package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Filter;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private List<Booking> checkStateByBooker(Filter state, long userId) {
        userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        switch (state) {
            case CURRENT:
                return bookingRepository.findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now())
                        .orElseThrow(BookingNotFoundException::new);
            case PAST:
                return bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now())
                        .orElseThrow(BookingNotFoundException::new);
            case ALL:
                return bookingRepository.findByBooker_IdOrderByStartDesc(userId)
                        .orElseThrow(BookingNotFoundException::new);
            case FUTURE:
                return bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now())
                        .orElseThrow(BookingNotFoundException::new);
            case WAITING:
                return bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, Status.WAITING)
                        .orElseThrow(BookingNotFoundException::new);
            case REJECTED:
                return bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, Status.REJECTED)
                        .orElseThrow(BookingNotFoundException::new);
            default:
                throw new FilterNotFoundException(state.toString());
        }
    }

    private List<Booking> checkStateByOwner(Filter state, long userId) {
        userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        switch (state) {
            case CURRENT:
                return bookingRepository.findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now())
                        .orElseThrow(BookingNotFoundException::new);
            case PAST:
                return bookingRepository.findByItem_Owner_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now())
                        .orElseThrow(BookingNotFoundException::new);
            case ALL:
                return bookingRepository.findByItem_Owner_IdOrderByStartDesc(userId)
                        .orElseThrow(BookingNotFoundException::new);
            case FUTURE:
                return bookingRepository.findByItem_Owner_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now())
                        .orElseThrow(BookingNotFoundException::new);
            case WAITING:
                return bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(userId, Status.WAITING)
                        .orElseThrow(BookingNotFoundException::new);
            case REJECTED:
                return bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(userId, Status.REJECTED)
                        .orElseThrow(BookingNotFoundException::new);
            default:
                throw new FilterNotFoundException(state.toString());
        }
    }

    @Override
    public List<Booking> getAllBookings(long userId, Filter state) {
        return checkStateByBooker(state, userId);
    }

    @Override
    public List<Booking> getAllBookingsByOwner(long userId, Filter state) {
        return checkStateByOwner(state, userId);
    }

    @Override
    public Booking createBooking(long userId, long itemId, Booking request) {
        request.validStartEnd();

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        request.setBooker(user);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFoundException::new);

        item.checkIsAvailable();
        item.checkIsOwner(userId);

        request.setItem(item);

        request.setStatus(Status.WAITING);
        return bookingRepository.save(request);
    }

    @Override
    public Booking updateBooking(long userId, long id, Boolean approved) {
        Booking booking = bookingRepository.findByIdAndBooker_IdOrIdAndItem_Owner_Id(id, userId, id, userId)
                .orElseThrow(BookingBadRequestExcetion::new);

        booking.checkIsNotOwner(userId);
        booking.approve(approved);

        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(long userId, long bookingId) {
        bookingRepository.delete(bookingRepository.findById(bookingId)
                .orElseThrow(BookingNotFoundException::new));
    }

    @Override
    public Booking getBookingById(long userId, long bookingId) {
        return bookingRepository.findByIdAndBooker_IdOrIdAndItem_Owner_Id(bookingId, userId, bookingId, userId)
                .orElseThrow(BookingNotFoundException::new);
    }

    @Override
    public Optional<Booking> getLastBookingById(long itemId, long userId, LocalDateTime end) {
        return bookingRepository.findFirstByItem_IdAndItem_Owner_IdAndStartBeforeAndStatusOrderByStartDesc(itemId, userId, end, Status.APPROVED);
    }

    @Override
    public Optional<Booking> getNextBookingById(long itemId, long userId, LocalDateTime start) {
        return bookingRepository.findFirstByItem_IdAndItem_Owner_IdAndStartAfterAndStatusOrderByStartAsc(itemId, userId, start, Status.APPROVED);
    }
}
