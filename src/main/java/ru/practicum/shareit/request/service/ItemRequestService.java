package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestResponseWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    List<ItemRequestResponseWithItemsDto> getAllItemRequests(long userId);

    List<ItemRequestResponseWithItemsDto> getAllItemRequests(long userId, int start, int size);

    ItemRequest createItemRequest(long userId,ItemRequest itemRequest);

    ItemRequestResponseWithItemsDto getItemRequestById(long id, long userId);
}