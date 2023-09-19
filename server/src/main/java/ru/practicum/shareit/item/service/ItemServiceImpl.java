package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.error.ItemNotFoundException;
import ru.practicum.shareit.error.UserNotFoundException;
import ru.practicum.shareit.helpers.Mapping;
import ru.practicum.shareit.helpers.Paging;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    private final ItemRequestRepository itemRequestRepository;

    private final UserRepository userRepository;

    private final BookingService bookingService;

    private final CommentService commentService;

    private final ModelMapper mapper;

    private final Paging paging = new Paging();

    private final Mapping mapping = new Mapping();

    public ItemResponseDto enrichResponse(Item item, long userId, LocalDateTime currentTime) {
        ItemResponseDto itemResponseDto = mapper.map(item, ItemResponseDto.class);
        bookingService.getLastBookingById(item.getId(), userId, currentTime).ifPresent(
                booking -> itemResponseDto.setLastBooking(mapper.map(booking, BookingRequestDto.class))
        );
        bookingService.getNextBookingById(item.getId(), userId, currentTime).ifPresent(
                booking -> itemResponseDto.setNextBooking(mapper.map(booking, BookingRequestDto.class))
        );
        List<CommentResponseDto> comments = mapping.mapList(
                commentService.getCommentByItem(userId, item.getId()),
                CommentResponseDto.class
        );
        itemResponseDto.setComments(comments);
        return itemResponseDto;
    }

    @Override
    public List<Item> getAllItems(long userId) {
        return itemRepository.findByOwner_IdOrderById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Page<Item> getAllItems(int start, int pageSize, long userId) {
        return itemRepository.findByOwner_IdOrderById(userId, paging.getPageable(start, pageSize))
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Item createItem(long userId, Item item, Long itemRequestId) {
        if (!item.getName().isBlank()) {
            item.setOwner(userRepository.findById(userId)
                    .orElseThrow(UserNotFoundException::new));
        }
        setRequest(item, itemRequestId);
        return itemRepository.save(item);
    }

    public void setRequest(Item item, Long itemRequestId) {
        // null better than '0'. '0' may be special type of itemRequests.
        // inside postman's tests the Item response struct include the NULL fields.
        Optional.ofNullable(itemRequestId).ifPresentOrElse(
                findId ->
                        item.setRequest(itemRequestRepository.findById(findId).orElse(null)),
                () ->
                        item.setRequest(null)
        );
    }

    @Override
    public Item updateItem(long userId, long id, ItemRequestDto request) {
        Item item = itemRepository.findByIdAndOwner_Id(id, userId)
                .orElseThrow(ItemNotFoundException::new);
        Optional.ofNullable(request.getName()).ifPresent(item::setName);
        Optional.ofNullable(request.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(request.getAvailable()).ifPresent(item::setAvailable);
        setRequest(item, request.getRequestId());
        return itemRepository.save(item);
    }

    @Override
    public void deleteItem(long userId, long id) {
        itemRepository.delete(itemRepository.findById(id)
                .orElseThrow(ItemNotFoundException::new));
    }

    @Override
    public Item getItemById(Long userId, long id) {
        return itemRepository.findById(id)
                .orElseThrow(ItemNotFoundException::new);
    }

    @Override
    public List<Item> search(long userId, String partOfName) {
        return itemRepository.search(partOfName)
                .orElseThrow(ItemNotFoundException::new);
    }

    @Override
    public Page<Item> search(int start, int pageSize, long userId, String partOfName) {
        return itemRepository.search(partOfName, paging.getPageable(start, pageSize))
                .orElseThrow(ItemNotFoundException::new);
    }
}


