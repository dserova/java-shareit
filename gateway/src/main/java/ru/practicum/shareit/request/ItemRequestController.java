package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    private final String userIdParameterName = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(userIdParameterName) long userId) {
        log.info("GET userId={}", userId);
        return itemRequestClient.getAllItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequestsByPage(@RequestHeader(userIdParameterName) long userId,
                                                           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                           @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("GET /all userId={}, from={}, size={}", userId, from, size);
        return itemRequestClient.getAllItemRequestsByPage(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable(name = "id") long itemRequestId,
                                                     @RequestHeader(userIdParameterName) long userId) {
        log.info("GET /{id} itemRequestId={}, userId={}", itemRequestId, userId);
        return itemRequestClient.getItemRequestById(userId, itemRequestId);
    }

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(userIdParameterName) long userId,
                                                    @Valid @RequestBody ItemRequestRequestDto itemRequestRequestDto) {
        log.info("POST userId={}, itemRequestRequestDto={}", userId, itemRequestRequestDto);
        return itemRequestClient.createItemRequest(userId, itemRequestRequestDto);
    }

}
