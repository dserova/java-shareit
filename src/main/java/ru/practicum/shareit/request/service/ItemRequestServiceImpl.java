package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.ItemRequestNotFoundException;
import ru.practicum.shareit.error.UserNotFoundException;
import ru.practicum.shareit.helpers.Mapping;
import ru.practicum.shareit.helpers.Paging;
import ru.practicum.shareit.item.dto.ItemResponseShortDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ModelMapper mapper;

    private final ItemRequestRepository itemRequestRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private final Paging paging = new Paging();
    private final Mapping mapping = new Mapping();


    private List<ItemRequestResponseWithItemsDto> getItemRequestResponseWithItemsDtos(List<ItemRequest> itemRequests) {
        return itemRequests.stream().map(this::getItemRequestResponseWithItemsDto
        ).collect(Collectors.toList());
    }

    private ItemRequestResponseWithItemsDto getItemRequestResponseWithItemsDto(ItemRequest itemRequest) {
        ItemRequestResponseWithItemsDto response = mapper.map(
                itemRequest,
                ItemRequestResponseWithItemsDto.class
        );

        List<ItemResponseShortDto> items = mapping.mapList(
                itemRepository.findByRequest_Id(response.getId()),
                ItemResponseShortDto.class
        );

        response.setItems(items);

        return response;
    }

    @Override
    @Transactional
    public List<ItemRequestResponseWithItemsDto> getAllItemRequests(long userId) {
        userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        List<ItemRequest> pageItems = itemRequestRepository.findByRequestor_Id(userId).orElseThrow();
        return getItemRequestResponseWithItemsDtos(pageItems);

    }

    @Override
    public List<ItemRequestResponseWithItemsDto> getAllItemRequests(long userId, int start, int pageSize) {
        Optional<Page<ItemRequest>> page = itemRequestRepository.findByRequestor_IdNot(userId, paging.getPageable(start, pageSize, "created"));
        List<ItemRequest> pageItems = page.orElseThrow().toList();
        return getItemRequestResponseWithItemsDtos(pageItems);

    }

    @Override
    @Transactional
    public ItemRequest createItemRequest(long userId, ItemRequest request) {
        request.setRequestor(
                userRepository.findById(userId)
                        .orElseThrow(UserNotFoundException::new)
        );
        return itemRequestRepository.save(request);
    }

    @Override
    @Transactional
    public ItemRequest updateItemRequest(long id, ItemRequestRequestDto request) {
        ItemRequest itemRequest = itemRequestRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return itemRequestRepository.save(itemRequest);
    }

    @Override
    @Transactional
    public void deleteItemRequest(long id) {
        itemRequestRepository.delete(itemRequestRepository.findById(id)
                .orElseThrow(UserNotFoundException::new));
    }

    @Override
    @Transactional
    public ItemRequestResponseWithItemsDto getItemRequestById(long id, long userId) {
        userRepository.findById(userId)
                .orElseThrow(ItemRequestNotFoundException::new);
        return getItemRequestResponseWithItemsDto(itemRequestRepository.findById(id)
                .orElseThrow(ItemRequestNotFoundException::new));
    }
}


