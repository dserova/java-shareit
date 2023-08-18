package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ModelMapper mapper;

    private final ItemService itemService;

    private final CommentService commentService;

    private final String userIdParameterName = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemResponseDto> getAllItems(@RequestParam(name = "from", defaultValue = "0") int from,@RequestParam(name = "size", defaultValue = "10") int size,@RequestHeader(userIdParameterName) long userId) {
        return itemService.getAllItems(from, size, userId).stream().map(
                item -> itemService.enrichResponse(item, userId, LocalDateTime.now())
        ).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ItemResponseDto getItemById(@RequestHeader(userIdParameterName) long userId, @PathVariable(name = "id") Long itemId) {
        Item item = itemService.getItemById(userId, itemId);
        return itemService.enrichResponse(item, userId, LocalDateTime.now());
    }

    @PostMapping
    public ItemRequestDto createItem(@RequestHeader(userIdParameterName) long userId, @RequestBody ItemRequestDto itemRequestDto) {
        Item request = mapper.map(itemRequestDto, Item.class);
        Item item = itemService.createItem(userId, request, itemRequestDto.getRequestId());
        return mapper.map(item, ItemRequestDto.class);
    }

    @PatchMapping("/{id}")
    public ItemRequestDto updateItem(@RequestHeader(userIdParameterName) long userId, @PathVariable(name = "id") long itemId, @RequestBody ItemRequestDto itemRequestDto) {
        Item item = itemService.updateItem(userId, itemId, itemRequestDto);
        return mapper.map(item, ItemRequestDto.class);
    }

    @DeleteMapping("/{id}")
    void deleteItem(@RequestHeader(userIdParameterName) long userId, @PathVariable(name = "id") Long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemRequestDto> search(@RequestParam(name = "from", defaultValue = "0") int from,@RequestParam(name = "size", defaultValue = "10") int size,@RequestHeader(userIdParameterName) long userId, @RequestParam(name = "text", defaultValue = "__{{ERROR_EXCEPTION}}__") String partOfName) {
        return itemService.search(from, size, userId, partOfName).stream().map(item -> mapper.map(item, ItemRequestDto.class)).collect(Collectors.toList());
    }

    @PostMapping("/{id}/comment")
    public CommentResponseDto createComment(@RequestHeader(userIdParameterName) long userId, @PathVariable(name = "id") long itemId, @RequestBody CommentRequestDto request) {
        Comment comment = commentService.createComment(userId, itemId, mapper.map(request, Comment.class));
        return mapper.map(comment, CommentResponseDto.class);
    }
}
