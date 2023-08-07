package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemService {
    List<Item> getAllItems(long userId);

    Item createItem(long userId, Item item);

    Item updateItem(long userId, long id, ItemRequestDto itemDtoRequest);

    void deleteItem(long userId, long id);

    Item getItemById(Long userId, long id);

    List<Item> search(long userId, String partOfName);

    ItemResponseDto enrichResponse(Item item, long userId, LocalDateTime currentTime);
}