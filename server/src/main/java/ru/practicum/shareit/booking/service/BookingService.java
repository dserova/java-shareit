package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Filter;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookingService {
    Page<Booking> getAllBookings(int start, int size, long userId, Filter state);

    Page<Booking> getAllBookingsByOwner(int start, int size, long userId, Filter state);

    Booking createBooking(long userId, long itemId, Booking booking);

    Booking updateBooking(long userId, long id, Boolean approved);

    void deleteBooking(long userId, long id);

    Booking getBookingById(long userId, long id);

    Optional<Booking> getLastBookingById(long id, long userId, LocalDateTime end);

    Optional<Booking> getNextBookingById(long id, long userId, LocalDateTime start);
}
