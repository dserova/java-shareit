package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.error.ItemNotFoundException;
import ru.practicum.shareit.error.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;
    private final BookingService bookingService;
    private final CommentService commentService;

    private final ModelMapper mapper;


    public ItemResponseDto enrichResponse(Item item, long userId, LocalDateTime currentTime) {
        ItemResponseDto itemResponseDto = mapper.map(item, ItemResponseDto.class);

        bookingService.getLastBookingById(item.getId(), userId, currentTime).ifPresent(booking -> itemResponseDto.setLastBooking(mapper.map(booking, BookingRequestDto.class)));

        bookingService.getNextBookingById(item.getId(), userId, currentTime).ifPresent(booking -> itemResponseDto.setNextBooking(mapper.map(booking, BookingRequestDto.class)));

        List<CommentResponseDto> comments = commentService.getCommentByItem(userId, item.getId()).stream().map(comment -> mapper.map(comment, CommentResponseDto.class)).collect(Collectors.toList());

        itemResponseDto.setComments(comments);

        return itemResponseDto;
    }

    @Override
    public List<Item> getAllItems(long userId) {
        return itemRepository.findByOwner_Id(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Item createItem(long userId, Item request) {
        if (!request.getName().isBlank()) {
            request.setOwner(userRepository.findById(userId)
                    .orElseThrow(UserNotFoundException::new));
        }
        return itemRepository.save(request);
    }

    @Override
    public Item updateItem(long userId, long id, ItemRequestDto request) {

        Item item = itemRepository.findByIdAndOwner_Id(id, userId)
                .orElseThrow(ItemNotFoundException::new);

        Optional.ofNullable(request.getName()).ifPresent(item::setName);
        Optional.ofNullable(request.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(request.getAvailable()).ifPresent(item::setAvailable);
        Optional.ofNullable(request.getRequest()).ifPresent(item::setRequest);

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
}


