package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    private final String userIdParameterName = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAllItems(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                              @RequestHeader(userIdParameterName) long userId) {
        log.info("GET userId={}, from={}, size={}", userId, from, size);
        return itemClient.getAllItems(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@RequestHeader(userIdParameterName) long userId,
                                              @PathVariable(name = "id") Long itemId) {
        log.info("GET /{id} userId={}, itemId={}", userId, itemId);
        return itemClient.getItemById(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(userIdParameterName) long userId,
                                             @RequestBody ItemRequestDto itemRequestDto) {
        log.info("POST userId={}, itemRequestDto={}", userId, itemRequestDto);
        return itemClient.createItem(userId, itemRequestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader(userIdParameterName) long userId,
                                             @PathVariable(name = "id") long itemId,
                                             @RequestBody ItemRequestDto itemRequestDto) {
        log.info("PATCH /{id} userId={}, itemId={}, itemRequestDto={}", userId, itemId, itemRequestDto);
        return itemClient.updateItem(userId, itemId, itemRequestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@RequestHeader(userIdParameterName) long userId,
                                             @PathVariable(name = "id") Long itemId) {
        log.info("DELETE /{id} userId={}, itemId={}", userId, itemId);
        return itemClient.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                         @RequestHeader(userIdParameterName) long userId,
                                         @RequestParam(name = "text", defaultValue = "") String text) {
        log.info("GET /search userId={}, from={}, size={}, text={}", userId, from, size, text);
        return itemClient.search(userId, from, size, text);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(userIdParameterName) long userId,
                                                @PathVariable(name = "id") long itemId,
                                                @RequestBody CommentRequestDto request) {
        log.info("POST /{id}/comment userId={}, itemId={}, request={}", userId, itemId, request);
        return itemClient.createComment(userId, itemId, request);
    }
}
