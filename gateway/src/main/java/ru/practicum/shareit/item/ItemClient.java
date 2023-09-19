package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestCreateDto;
import ru.practicum.shareit.item.dto.ItemRequestUpdateDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    private static final String delimiter = "/";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getAllItems(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getItemById(long userId, Long itemId) {
        return get(delimiter + itemId, userId);
    }

    public ResponseEntity<Object> createItem(long userId, ItemRequestCreateDto itemRequestCreateDto) {
        return post("", userId, itemRequestCreateDto);
    }

    public ResponseEntity<Object> updateItem(long userId, long itemId, ItemRequestUpdateDto itemRequestUpdateDto) {
        return patch(delimiter + itemId, userId, itemRequestUpdateDto);
    }

    public ResponseEntity<Object> deleteItem(long userId, Long itemId) {
        return delete(delimiter + itemId, userId);
    }

    public ResponseEntity<Object> search(long userId, int from, int size, String text) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size,
                "text", text

        );
        return get(delimiter + "search?text={text}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> createComment(long userId, long itemId, CommentRequestDto request) {
        return post(delimiter + itemId + "/comment", userId, request);
    }
}
