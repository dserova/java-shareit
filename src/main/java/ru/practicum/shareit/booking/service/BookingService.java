package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Filter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingService {

    List<Booking> getAllBookings(long userId, Filter state);

    List<Booking> getAllBookingsByOwner(long userId, Filter state);

    Booking createBooking(long userId, long itemId, Booking booking);

    Booking updateBooking(long userId, long id, Boolean approved);

    void deleteBooking(long userId, long id);

    Booking getBookingById(long userId, long id);

    Optional<Booking> getLastBookingById(long id, long userId, LocalDateTime end);

    Optional<Booking> getNextBookingById(long id, long userId, LocalDateTime start);
}
