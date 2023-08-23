package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Filter;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final ModelMapper mapper;

    private final BookingService bookingService;

    private final String userIdParameterName = "X-Sharer-User-Id";

    @GetMapping("/{id}")
    public BookingResponseDto getBookingById(@RequestHeader(userIdParameterName) long userId, @PathVariable(name = "id") Long bookingId) {
        Booking booking = bookingService.getBookingById(userId, bookingId);
        return mapper.map(booking, BookingResponseDto.class);
    }

    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader(userIdParameterName) long userId, @RequestBody BookingRequestDto bookingRequestDto) {
        Booking request = mapper.map(bookingRequestDto, Booking.class);
        Booking booking = bookingService.createBooking(userId, bookingRequestDto.getItemId(), request);
        return mapper.map(booking, BookingResponseDto.class);
    }

    @GetMapping
    public List<BookingResponseDto> getAllBooking(@RequestParam(name = "from", defaultValue = "0") int from, @RequestParam(name = "size", defaultValue = "10") int size, @RequestHeader(userIdParameterName) long userId, @RequestParam(name = "state", defaultValue = "ALL") Filter state) {
        return bookingService.getAllBookings(from, size, userId, state).stream().map(
                booking -> mapper.map(booking, BookingResponseDto.class)
        ).collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllBookingByOwner(@RequestParam(name = "from", defaultValue = "0") int from, @RequestParam(name = "size", defaultValue = "10") int size, @RequestHeader(userIdParameterName) long userId, @RequestParam(name = "state", defaultValue = "ALL") Filter state) {
        return bookingService.getAllBookingsByOwner(from, size, userId, state).stream().map(
                booking -> mapper.map(booking, BookingResponseDto.class)
        ).collect(Collectors.toList());
    }

    @PatchMapping("/{id}")
    public BookingResponseDto updateItem(@RequestHeader(userIdParameterName) long userId, @RequestParam(name = "approved", defaultValue = "false") Boolean approved, @PathVariable(name = "id") long bookingId) {
        Booking booking = bookingService.updateBooking(userId, bookingId, approved);
        return mapper.map(booking, BookingResponseDto.class);
    }

    @DeleteMapping("/{id}")
    void deleteItem(@RequestHeader(userIdParameterName) long userId, @PathVariable(name = "id") long bookingId) {
        bookingService.deleteBooking(userId, bookingId);
    }
}
