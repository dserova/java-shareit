package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
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

    private final BookingService bookingService;

    private final CommentService commentService;

    private final String userIdParameterName = "X-Sharer-User-Id";

    private ItemResponseDto EnrichResponse(Item item, long userId, LocalDateTime currentTime) {
        ItemResponseDto itemResponseDto = mapper.map(item, ItemResponseDto.class);
        bookingService.getLastBookingById(item.getId(), userId, currentTime).ifPresent(booking -> itemResponseDto.setLastBooking(mapper.map(booking, BookingRequestDto.class)));

        bookingService.getNextBookingById(item.getId(), userId, currentTime).ifPresent(booking -> itemResponseDto.setNextBooking(mapper.map(booking, BookingRequestDto.class)));

        List<CommentResponseDto> comments = commentService.getCommentByItem(userId, item.getId()).stream().map(comment -> mapper.map(comment, CommentResponseDto.class)).collect(Collectors.toList());

        itemResponseDto.setComments(comments);

        return itemResponseDto;
    }

    @GetMapping
    public List<ItemResponseDto> getAllItems(@RequestHeader(userIdParameterName) long userId) {
        return itemService.getAllItems(userId).stream().map(item -> EnrichResponse(item, userId, LocalDateTime.now())).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ItemResponseDto getItemById(@RequestHeader(userIdParameterName) long userId, @PathVariable(name = "id") Long itemId) {
        Item item = itemService.getItemById(userId, itemId);
        return EnrichResponse(item, userId, LocalDateTime.now());
    }

    @PostMapping
    public ItemRequestDto createItem(@RequestHeader(userIdParameterName) long userId, @RequestBody ItemRequestDto itemRequestDto) {
        Item request = mapper.map(itemRequestDto, Item.class);
        Item item = itemService.createItem(userId, request);
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
    public List<ItemRequestDto> getItemByName(@RequestHeader(userIdParameterName) long userId, @RequestParam(name = "text", defaultValue = "__{{ERROR_EXCEPTION}}__") String partOfName) {
        return itemService.getItemByName(userId, partOfName).stream().map(item -> mapper.map(item, ItemRequestDto.class)).collect(Collectors.toList());
    }

    // Comment endpoint
    @PostMapping("/{id}/comment")
    public CommentResponseDto createComment(@RequestHeader(userIdParameterName) long userId, @PathVariable(name = "id") long itemId, @RequestBody CommentRequestDto request) {
        Comment comment = commentService.createComment(userId, itemId, mapper.map(request, Comment.class));
        return mapper.map(comment, CommentResponseDto.class);
    }
}
