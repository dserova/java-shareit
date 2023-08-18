package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ModelMapper mapper;

    private final ItemRequestService itemRequestService;
    private final String userIdParameterName = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemRequestResponseWithItemsDto> getAllItemRequests(@RequestHeader(userIdParameterName) long userId) {
        return itemRequestService.getAllItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseWithItemsDto> getItemRequestById(@RequestHeader(userIdParameterName) long userId, @RequestParam(name = "from", defaultValue = "0") int from, @RequestParam(name = "size", defaultValue = "10") int size) {
        return itemRequestService.getAllItemRequests(userId, from, size);
    }

    @GetMapping("/{id}")
    public ItemRequestResponseWithItemsDto getItemRequestById(@PathVariable(name = "id") long id, @RequestHeader(userIdParameterName) long userId) {
        return itemRequestService.getItemRequestById(id, userId);
    }

    @PostMapping
    public ItemRequestResponseDto createItemRequest(@RequestHeader(userIdParameterName) long userId, @RequestBody ItemRequestRequestDto itemRequestRequestDto) {
        ItemRequest request = mapper.map(itemRequestRequestDto, ItemRequest.class);
        ItemRequest itemRequest = itemRequestService.createItemRequest(userId, request);
        return mapper.map(itemRequest, ItemRequestResponseDto.class);
    }

//    @PatchMapping("/{id}")
//    public ItemRequestResponseDto updateItemRequest(@PathVariable(name = "id") long id, @RequestBody ItemRequestRequestDto itemRequestRequestDto) {
//        ItemRequest itemRequest = itemRequestService.updateItemRequest(id, itemRequestRequestDto);
//        return mapper.map(itemRequest, ItemRequestResponseDto.class);
//    }
//
//    @DeleteMapping("/{id}")
//    void deleteItemRequest(@PathVariable(name = "id") Long id) {
//        itemRequestService.deleteItemRequest(id);
//    }
}
