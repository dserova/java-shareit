package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ModelMapper mapper;

    private final ItemService service;

    private final String userIdParameterName = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(userIdParameterName) long userId) {
        return service.getAllItems(userId).stream().map(item -> mapper.map(item, ItemDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@RequestHeader(userIdParameterName) long userId, @PathVariable(name = "id") Long id) {
        Item item = service.getItemById(userId, id);
        return mapper.map(item, ItemDto.class);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(userIdParameterName) long userId, @RequestBody ItemDto request) {
        Item itemRequest = mapper.map(request, Item.class);
        Item item = service.createItem(userId, itemRequest);
        return mapper.map(item, ItemDto.class);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader(userIdParameterName) long userId, @PathVariable long id, @RequestBody ItemDto request) {
        Item item = service.updateItem(userId, id, request);
        return mapper.map(item, ItemDto.class);
    }

    @DeleteMapping("/{id}")
    void deleteItem(@RequestHeader(userIdParameterName) long userId, @PathVariable(name = "id") Long id) {
        service.deleteItem(userId, id);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByName(@RequestHeader(userIdParameterName) long userId, @RequestParam(name = "text", defaultValue = "__{{ERROR_EXCEPTION}}__") String partOfName) {
        return service.getItemByName(userId, partOfName).stream().map(item -> mapper.map(item, ItemDto.class))
                .collect(Collectors.toList());
    }
}
