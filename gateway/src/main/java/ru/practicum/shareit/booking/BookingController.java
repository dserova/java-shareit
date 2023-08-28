package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.Filter;
import ru.practicum.shareit.error.BookingBadRequestException;
import ru.practicum.shareit.error.FilterNotFoundException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    private final String userIdParameterName = "X-Sharer-User-Id";

    public void validStartEnd(BookingRequestDto booking) {
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new BookingBadRequestException();
        }
        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingBadRequestException();
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new BookingBadRequestException();
        }
        if (booking.getEnd().isEqual(booking.getStart())) {
            throw new BookingBadRequestException();
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookings(@RequestHeader(userIdParameterName) long userId,
                                                 @RequestParam(name = "state", defaultValue = "all") String state,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Filter filter = Filter.from(state)
                .orElseThrow(() -> new FilterNotFoundException(state));
        log.info("GET state={}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getAllBookings(userId, filter, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsByOwner(@RequestHeader(userIdParameterName) long userId,
                                                        @RequestParam(name = "state", defaultValue = "all") String state,
                                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Filter filter = Filter.from(state)
                .orElseThrow(() -> new FilterNotFoundException(state));
        log.info("GET /owner state={}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getAllBookingsByOwner(userId, filter, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(userIdParameterName) long userId,
                                                 @PathVariable(name = "id") Long bookingId) {
        log.info("GET /{id} bookingId={}, userId={}", bookingId, userId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(userIdParameterName) long userId,
                                                @RequestBody BookingRequestDto bookingRequestDto) {
        validStartEnd(bookingRequestDto);
        log.info("POST bookingId={}, userId={}", bookingRequestDto, userId);
        return bookingClient.createBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateBooking(@RequestHeader(userIdParameterName) long userId,
                                                @RequestParam(name = "approved", defaultValue = "false") Boolean approved,
                                                @PathVariable(name = "id") long bookingId) {
        log.info("PATCH /{id} bookingId={}, userId={}, approved={}", bookingId, userId, approved);
        return bookingClient.updateBooking(userId, bookingId, approved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBooking(@RequestHeader(userIdParameterName) long userId,
                                                @PathVariable(name = "id") long bookingId) {
        log.info("DELETE /{id} bookingId={}, userId={}", bookingId, userId);
        return bookingClient.deleteBooking(userId, bookingId);
    }
}
