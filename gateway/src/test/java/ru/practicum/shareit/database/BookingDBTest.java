package ru.practicum.shareit.database;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.error.FilterNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(classes = ShareItGateway.class)
public class BookingDBTest {

    private final BookingController bookingController;

    @Test
    void getAllBookingsFail() {
        try {
            bookingController.getAllBookings(1, "AbRaCaDaBrA", 0, 1);
            throw new RuntimeException();
        } catch (FilterNotFoundException e) {
            assertThat(e.getClass(), equalTo(FilterNotFoundException.class));
        }
    }

    @Test
    void getAllBookingsByOwnerFail() {
        try {
            bookingController.getAllBookingsByOwner(1, "AbRaCaDaBrA", 0, 1);
            throw new RuntimeException();
        } catch (FilterNotFoundException e) {
            assertThat(e.getClass(), equalTo(FilterNotFoundException.class));
        }
    }
}






