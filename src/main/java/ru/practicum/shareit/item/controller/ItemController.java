package ru.practicum.shareit.item.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ModelMapper modelMapper;

    private final ItemService itemService;

    private final String XParam = "X-Sharer-User-Id";

    public ItemController(ItemService itemService, UserRepository userRepository) {
        super();
        this.itemService = itemService;
    }

    @GetMapping
    public List<Item> getAllItems(@RequestHeader(XParam) long userId) {
        return itemService.getAllItems(userId).stream().map(item -> modelMapper.map(item, Item.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Item getItemById(@RequestHeader(XParam) long userId, @PathVariable(name = "id") Long id) {
        Item item = itemService.getItemById(userId, id);
        return modelMapper.map(item, Item.class);
    }

    @PostMapping
    public Item createItem(@RequestHeader(XParam) long userId, @RequestBody ItemDto itemDto) {
        Item itemRequest = modelMapper.map(itemDto, Item.class);
        return itemService.createItem(userId, itemRequest);
    }

    @PatchMapping("/{id}")
    public Item updateItem(@RequestHeader(XParam) long userId, @PathVariable long id, @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, id, itemDto);
    }

    @DeleteMapping("/{id}")
    void deleteItem(@RequestHeader(XParam) long userId, @PathVariable(name = "id") Long id) {
        itemService.deleteItem(userId, id);
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestHeader(XParam) long userId, @RequestParam(name = "text") String partOfName) {
        return itemService.searchByName(userId, partOfName);
    }
}
