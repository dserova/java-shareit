package ru.practicum.shareit.postman;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.practicum.shareit.ShareItApp;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest(classes = ShareItApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class IntegrationPostmanTest {

    // deleteTimeStamp clean timestamp
    String deleteTimeStamp(String text) {
        String regex = "(,)*[\\r\\n].+\"(timestamp|start|end|created)\": \".{19,27}\"(,)*";
        return text.replaceAll(regex, "");
    }

    void delay(String text) {
        String regex = "(setTimeout)\\D+(\\d{4})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            long d = Long.parseLong(matcher.group(2));
            try {
                Thread.sleep(d);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    LocalDateTime changeTime(String text, String name, LocalDateTime time) {
        String regex = "(start|end).+(\\D)(\\d{1,3}).+('\\w')";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            if (matcher.group(1).equals(name)) {
                boolean m = false;
                if (matcher.group(2) != null) {
                    m = matcher.group(2).equals("-");
                }
                long val = Long.parseLong(matcher.group(3));
                switch (matcher.group(4)) {
                    case "'s'":
                        if (m) {
                            time = time.minusSeconds(val);
                        } else {
                            time = time.plusSeconds(val);
                        }
                        break;
                    case "'m'":
                        if (m) {
                            time = time.minusMinutes(val);
                        } else {
                            time = time.plusMinutes(val);
                        }
                        break;
                    case "'h'":
                        if (m) {
                            time = time.minusHours(val);
                        } else {
                            time = time.plusHours(val);
                        }
                        break;
                    case "'d'":
                        if (m) {
                            time = time.minusDays(val);
                        } else {
                            time = time.plusDays(val);
                        }
                        break;
                }
            }
        }
        return time;
    }

    // checkByPostmanExportFileCollection parsing the postman export file
    void checkByPostmanExportFileCollection(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode collection = mapper.readTree(new File(filePath));
        String name = collection.get("info").get("name").asText();
        System.out.println("Collection name: " + name);
        int counter = 0;
        for (JsonNode folder : collection.get("item")) {
            String folderName = folder.get("name").asText();
            JsonNode requestsFolder = folder.get("item");
            for (JsonNode request1 : requestsFolder) {
                String reqcontent = "*/*";
                String reqx001 = "";
                String rescontent = "*/*";
                String reqbody = "";
                String resbody = "";
                int reqCode = 102;
                String reqStatus = "";

                JsonNode request = request1.get("request");
                String description = folderName + " --> " + request1.get("name").asText();
                String endpoint = request.get("url").get("raw").asText();
                JsonNode paths = request.get("url").get("host");
                for (JsonNode path : paths) {
                    endpoint = endpoint.replace(path.asText(), "");
                    if (request.get("url").has("variable")) {
                        for (JsonNode v : request.get("url").get("variable")) {
                            endpoint = endpoint.replace(":" + v.get("key").asText(), v.get("value").asText());
                        }
                    }
                }

                // get envs
                LocalDateTime start = LocalDateTime.now();
                LocalDateTime end = LocalDateTime.now();
                JsonNode events = request1.get("event");
                for (JsonNode event : events) {
                    if (event.get("script").has("exec") && event.get("listen").asText().equals("prerequest")) {
                        for (JsonNode v : event.get("script").get("exec")) {
                            start = changeTime(v.asText(), "start", start);
                            end = changeTime(v.asText(), "end", end);
                            delay(v.asText());
                        }
                    }
                }

                String reqMethod = request.get("method").asText();
                JsonNode requestHeaders = request.get("header");
                for (JsonNode header : requestHeaders) {
                    if (header.get("key").asText().equals("Content-Type")) {
                        reqcontent = header.get("value").asText();
                    }

                    if (header.has("disabled") && header.get("disabled").asBoolean()) {
                        // disable X
                    } else {
                        if (header.get("key").asText().equals("X-Sharer-User-Id")) {
                            reqx001 = header.get("value").asText();
                        }
                    }
                }

                if (request.has("body")) {
                    if (request.get("body").has("raw")) {
                        reqbody = request.get("body").get("raw").asText();
                        // set envs
                        reqbody = reqbody.replaceAll("\\{\\{start.*\\}\\}", start.toString());
                        reqbody = reqbody.replaceAll("\\{\\{end.*\\}\\}", end.toString());
                    }
                }

                if (request1.has("response") && !request1.get("response").isEmpty()) {
                    JsonNode responses = request1.get("response");
                    for (JsonNode response : responses) {
                        reqCode = response.get("code").asInt();
                        reqStatus = response.get("status").asText();
                        JsonNode responseHeaders = response.get("header");
                        for (JsonNode header : responseHeaders) {
                            if (header.get("key").asText().equals("Content-Type")) {
                                rescontent = header.get("value").asText();
                            }
                        }
                        if (response.has("body")) {
                            resbody = response.get("body").asText();
                            if (resbody.equals("null")) {
                                resbody = "";
                            }
                        }
                        break;
                    }
                } else {
                    continue;
                }

                MediaType reqMedia = MediaType.valueOf(reqcontent);
                MediaType resMedia = MediaType.valueOf(rescontent);

                if (reqbody.isEmpty() && reqcontent.equals("*/*")) {
                    reqMedia = MediaType.TEXT_PLAIN;
                }

                if (reqbody.contains("{") && reqcontent.equals("*/*")) {
                    reqMedia = MediaType.APPLICATION_JSON;
                }

                if (reqbody.isEmpty() && reqcontent.equals("application/json")) {
                    reqbody = "{}";
                }

                if ((reqMethod.equals("PATCH") || reqMethod.equals("DELETE")) && resbody.isEmpty() && rescontent.equals("*/*")) {
                    resMedia = null;
                }

                if (resbody.isEmpty() && rescontent.equals("application/json")) {
                    resbody = "{}";
                }

                // language fix
                // resbody = resbody.replaceAll("must be a well-formed email address", "должно иметь формат адреса электронной почты");
                // resbody = resbody.replaceAll("must not be blank", "не должно быть пустым");

                // not allowed timestamp in struct compare
                resbody = deleteTimeStamp(resbody);
                System.out.println("\n\n============================================\n\n");
                System.out.println("===== 1) POSTMAN [" + description + "] =====");

                System.out.println(">>>>> REQUEST");
                System.out.println("> Method/API: " + reqMethod + " [" + endpoint + "]");
                System.out.println("> Content-Type: " + reqcontent);
                System.out.println("> X-Sharer-User-Id: " + reqx001);
                System.out.println("> Body: \n" + reqbody);
                System.out.println("\n");

                System.out.println("<<<<< RESPONSE");
                System.out.println("< Status/Code: " + reqStatus + " [" + reqCode + "]");
                System.out.println("< Content-Type: " + rescontent);
                System.out.println("< Body: \n" + resbody);
                System.out.println("\n");

                this.template(description,
                        endpoint,
                        reqbody,
                        resbody,
                        reqx001,
                        reqMedia,
                        resMedia,
                        HttpStatus.valueOf(reqCode),
                        HttpMethod.valueOf(reqMethod)
                );
                counter++;
            }
        }
        System.out.println("TESTS COUNT: " + counter);
    }

    @Value("${local.server.port}")
    private Integer port;

    void template(String description, String endpoint, String RequestBody, String ResponseBody, String RequestX, MediaType RequestType, MediaType ResponseType, HttpStatus StatusResponse, HttpMethod method) throws RuntimeException {
        String userId = "X-Sharer-User-Id";
        if (RequestX.isBlank()) {
            userId = "DEPRECATED-X-Sharer-User-Id";
        }
        System.out.println("http://localhost:" + port + endpoint);
        // https://docs.spring.io/spring-framework/reference/testing/webtestclient.html
        // Test with WebTest
        WebTestClient client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        client.method(method).uri(endpoint)
                .contentType(RequestType)
                .header(userId, RequestX)
                .bodyValue(RequestBody)
                .exchange()
                .expectAll(
                        // check status code
                        spec -> spec.expectStatus().isEqualTo(StatusResponse),
                        // check content-type
                        spec -> spec.expectHeader().contentType(ResponseType),
                        // check body
                        spec -> {
                            if (ResponseBody.equals("true") || ResponseBody.equals("false")) {
                                spec.expectBody(Boolean.class).isEqualTo(ResponseBody.equals("true"));
                            } else {
                                spec.expectBody().json(ResponseBody);
                            }
                        },
                        spec -> {
                            System.out.println("===== 2) WebTestClient [" + description + "] =====");
                            System.out.println(spec.returnResult(Response.class));
                        }
                );
    }

    @Test
    void caseTests15() throws IOException {
        // sprint15
        // TESTS COUNT: 139
        checkByPostmanExportFileCollection("postman/Sprint 15 ShareIt (add-item-requests).json");
    }

}

