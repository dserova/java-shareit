package ru.practicum.shareit;

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

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@SpringBootTest(classes = ShareItApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ShareItTests {


    String deleteTimeStamp(String text) {
        String regex = "\"timestamp\": \\d{12,},";
        return text.replaceAll(regex, "");
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

                if (reqbody.isEmpty() && reqcontent.equals("application/json")) {
                    reqbody = "{}";
                }

                if ((reqMethod.equals("PATCH") || reqMethod.equals("DELETE")) && resbody.isEmpty() && rescontent.equals("*/*")) {
                    resMedia = null;
                }

                if (resbody.isEmpty() && rescontent.equals("application/json")) {
                    resbody = "{}";
                }

                if (!resbody.isEmpty() && (reqCode < 200 || reqCode >= 300) && rescontent.equals("application/json")) {
                    resbody = "{}";
                }
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
                .header("Content-Type", RequestType.toString())
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
    void caseTests13() throws IOException, SQLException {
        // sprint13
        // TESTS COUNT: 38
        checkByPostmanExportFileCollection("postman/Sprint 13 ShareIt (add-controllers).json");
    }

}
