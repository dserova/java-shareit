package ru.practicum.shareit.database;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Filter;
import ru.practicum.shareit.booking.model.FilterImpl;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.error.FilterNotFoundException;
import ru.practicum.shareit.helpers.Generate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;

@ExtendWith(MockitoExtension.class)
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BookingDBTest {

    private final BookingService bookingService;

    private final UserService userService;

    private final ItemRequestService itemRequestService;

    private final ItemService itemService;

    private final Booking booking = new Generate().random(Booking.class);

    Booking before() {
        User nub = userService.createUser(booking.getBooker());
        User nu = userService.createUser(booking.getItem().getOwner());
        User nru = userService.createUser(booking.getItem().getRequest().getRequestor());
        ItemRequest nir = itemRequestService.createItemRequest(nru.getId(), booking.getItem().getRequest());
        Item ni = itemService.createItem(nu.getId(), booking.getItem(), nir.getId());
        Booking b = bookingService.createBooking(nub.getId(), ni.getId(), booking);
        booking.setStatus(Status.APPROVED);
        return bookingService.updateBooking(nu.getId(), b.getId(), true);
    }

    @Test
    void createBooking() {
        Booking newItem = before();
        Booking resultElement = bookingService.getBookingById(newItem.getBooker().getId(), newItem.getId());
        assertThat(resultElement.getId(), notNullValue());
        assertThat(resultElement.getName(), equalTo(booking.getName()));
        assertThat(resultElement.getDescription(), equalTo(booking.getDescription()));
    }

    @Test
    void getAllBookings() {
        Booking newItem = before();
        Page<Booking> resultElement = bookingService.getAllBookings(0, 1, newItem.getBooker().getId(), FilterImpl.ALL);
        assertThat(resultElement.getContent().get(0).getId(), notNullValue());
        assertThat(resultElement.getContent().get(0).getName(), equalTo(booking.getName()));
        assertThat(resultElement.getContent().get(0).getDescription(), equalTo(booking.getDescription()));
        assertThat(resultElement.getContent().get(0).getStatus(), equalTo(booking.getStatus()));
        assertThat(resultElement.getContent().get(0).getStart(), equalTo(booking.getStart()));
        assertThat(resultElement.getContent().get(0).getEnd(), equalTo(booking.getEnd()));
    }

    @Test
    void getAllBookingsByOwner() {
        Booking newItem = before();
        Page<Booking> resultElement = bookingService.getAllBookingsByOwner(0, 1, newItem.getItem().getOwner().getId(), FilterImpl.ALL);
        assertThat(resultElement.getContent().get(0).getId(), notNullValue());
        assertThat(resultElement.getContent().get(0).getName(), equalTo(booking.getName()));
        assertThat(resultElement.getContent().get(0).getDescription(), equalTo(booking.getDescription()));
        assertThat(resultElement.getContent().get(0).getStatus(), equalTo(booking.getStatus()));
        assertThat(resultElement.getContent().get(0).getStart(), equalTo(booking.getStart()));
        assertThat(resultElement.getContent().get(0).getEnd(), equalTo(booking.getEnd()));
    }

    @Test
    void getBooking() {
        Booking newItem = before();
        Booking resultElement = bookingService.getNextBookingById(newItem.getItem().getId(), newItem.getItem().getOwner().getId(), LocalDateTime.now()).orElseThrow();
        assertThat(resultElement.getId(), notNullValue());
        assertThat(resultElement.getName(), equalTo(booking.getName()));
        assertThat(resultElement.getDescription(), equalTo(booking.getDescription()));
        assertThat(resultElement.getStatus(), equalTo(booking.getStatus()));
        assertThat(resultElement.getStart(), equalTo(booking.getStart()));
        assertThat(resultElement.getEnd(), equalTo(booking.getEnd()));
    }

    @Test
    void deleteBooking() {
        Booking newItem = before();
        bookingService.deleteBooking(newItem.getItem().getOwner().getId(), newItem.getId());
        Booking resultElement = bookingService.getNextBookingById(newItem.getItem().getId(), newItem.getItem().getOwner().getId(), LocalDateTime.now()).orElse(null);
        assertThat(resultElement, nullValue());
    }

    @Test
    void getAllBookingsFail() {
        Filter dummyFilter = Mockito.mock(Filter.class);
        Booking newItem = before();
        try {
            bookingService.getAllBookings(0, 1, newItem.getItem().getOwner().getId(), dummyFilter);
            throw new RuntimeException();
        } catch (FilterNotFoundException e) {
            assertThat(e.getClass(), equalTo(FilterNotFoundException.class));
        }
    }

    @Test
    void getAllBookingsByOwnerFail() {
        Filter dummyFilter = Mockito.mock(Filter.class);
        Booking newItem = before();
        try {
            bookingService.getAllBookingsByOwner(0, 1, newItem.getItem().getOwner().getId(), dummyFilter);
            throw new RuntimeException();
        } catch (FilterNotFoundException e) {
            assertThat(e.getClass(), equalTo(FilterNotFoundException.class));
        }
    }
}






