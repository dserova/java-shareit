package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<Item> getAllItems(long userId);

    Item createItem(long userId, Item item);

    Item updateItem(long userId, long id, ItemDto itemRequest);

    void deleteItem(long userId, long id);

    Item getItemById(long userId, long id);

    List<Item> searchByName(long userId, String partOfName);
}