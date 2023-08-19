package ru.practicum.shareit.database;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.error.ItemNotFoundException;
import ru.practicum.shareit.helpers.Generate;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemDBTest {
    private final UserService userService;
    private final ItemRequestService itemRequestService;
    private final ItemService itemService;

    private final CommentService commentService;

    private final BookingService bookingService;

    final ItemRequestDto itemUpdate = new Generate().random(ItemRequestDto.class);

    private final Item item = new Generate().random(Item.class);

    private final Comment comment = new Generate().random(Comment.class);

    private final Booking booking = new Generate().random(Booking.class);

    Item before() {
        User nub = userService.createUser(booking.getBooker());
        User nu = userService.createUser(booking.getItem().getOwner());
        User nru = userService.createUser(booking.getItem().getRequest().getRequestor());
        ItemRequest nir = itemRequestService.createItemRequest(nru.getId(), booking.getItem().getRequest());
        Item ni = itemService.createItem(nu.getId(), booking.getItem(), nir.getId());
        booking.setStart(LocalDateTime.now().plusSeconds(1));
        booking.setEnd(LocalDateTime.now().plusSeconds(2));
        Booking b = bookingService.createBooking(nub.getId(), ni.getId(), booking);
        booking.setStatus(Status.APPROVED);
        bookingService.updateBooking(nu.getId(), b.getId(), true);
        return ni;
    }

    Item beforeWithComment() {
        User nub = userService.createUser(booking.getBooker());
        User nu = userService.createUser(booking.getItem().getOwner());
        User nru = userService.createUser(booking.getItem().getRequest().getRequestor());
        ItemRequest nir = itemRequestService.createItemRequest(nru.getId(), booking.getItem().getRequest());
        Item ni = itemService.createItem(nu.getId(), booking.getItem(), nir.getId());
        booking.setStart(LocalDateTime.now().plusSeconds(1));
        booking.setEnd(LocalDateTime.now().plusSeconds(2));
        Booking b = bookingService.createBooking(nub.getId(), ni.getId(), booking);
        booking.setStatus(Status.APPROVED);
        Booking nb = bookingService.updateBooking(nu.getId(), b.getId(), true);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        comment.setItem(ni);
        comment.setAuthor(nub);
        commentService.createComment(nb.getBooker().getId(), ni.getId(), comment);
        return ni;
    }

    @Test
    void createItem() {
        Item newItem = before();
        Item resultElement = itemService.getItemById(newItem.getOwner().getId(), newItem.getId());
        assertThat(resultElement.getId(), notNullValue());
        assertThat(resultElement.getName(), equalTo(booking.getItem().getName()));
        assertThat(resultElement.getDescription(), equalTo(booking.getItem().getDescription()));
        assertThat(resultElement.getOwner(), equalTo(booking.getItem().getOwner()));
    }

    @Test
    void getAllItemsByPage() {
        Page<Item> resultElement = itemService.getAllItems(0, 1, before().getOwner().getId());
        assertThat(resultElement.getContent().get(0).getId(), notNullValue());
        assertThat(resultElement.getContent().get(0).getName(), equalTo(booking.getItem().getName()));
        assertThat(resultElement.getContent().get(0).getDescription(), equalTo(booking.getItem().getDescription()));
        assertThat(resultElement.getContent().get(0).getOwner(), equalTo(booking.getItem().getOwner()));
    }

    @Test
    void getAllItems() {
        List<Item> resultElement = itemService.getAllItems(before().getOwner().getId());
        assertThat(resultElement.get(0).getId(), notNullValue());
        assertThat(resultElement.get(0).getName(), equalTo(booking.getItem().getName()));
        assertThat(resultElement.get(0).getDescription(), equalTo(booking.getItem().getDescription()));
        assertThat(resultElement.get(0).getOwner(), equalTo(booking.getItem().getOwner()));
    }

    @Test
    void getItem() {
        Item newItem = before();
        Item resultElement = itemService.getItemById(newItem.getOwner().getId(), newItem.getId());
        assertThat(resultElement.getId(), notNullValue());
        assertThat(resultElement.getName(), equalTo(booking.getItem().getName()));
        assertThat(resultElement.getDescription(), equalTo(booking.getItem().getDescription()));
        assertThat(resultElement.getOwner(), equalTo(booking.getItem().getOwner()));
    }

    @Test
    void updateItem() {
        Item newItem = before();
        itemService.updateItem(newItem.getOwner().getId(), newItem.getId(), itemUpdate);
        Page<Item> resultElement = itemService.getAllItems(0, 1, newItem.getOwner().getId());
        assertThat(resultElement.getContent().get(0).getId(), notNullValue());
        assertThat(resultElement.getContent().get(0).getName(), equalTo(booking.getItem().getName()));
        assertThat(resultElement.getContent().get(0).getDescription(), equalTo(booking.getItem().getDescription()));
        assertThat(resultElement.getContent().get(0).getOwner(), equalTo(booking.getItem().getOwner()));
    }

    @Test
    void searchItemByPage() {
        Item newItem = before();
        newItem.setAvailable(true);
        Page<Item> resultElement = itemService.search(0, 1, 0, newItem.getName());
        assertThat(resultElement.getContent().get(0).getId(), notNullValue());
        assertThat(resultElement.getContent().get(0).getName(), equalTo(booking.getItem().getName()));
        assertThat(resultElement.getContent().get(0).getDescription(), equalTo(booking.getItem().getDescription()));
        assertThat(resultElement.getContent().get(0).getOwner(), equalTo(booking.getItem().getOwner()));
    }

    @Test
    void searchItem() {
        Item newItem = before();
        newItem.setAvailable(true);
        List<Item> resultElement = itemService.search(0, newItem.getName());
        assertThat(resultElement.get(0).getId(), notNullValue());
        assertThat(resultElement.get(0).getName(), equalTo(booking.getItem().getName()));
        assertThat(resultElement.get(0).getDescription(), equalTo(booking.getItem().getDescription()));
        assertThat(resultElement.get(0).getOwner(), equalTo(booking.getItem().getOwner()));
    }

    @Test
    void searchItemFail() {
        Item newItem = before();
        newItem.setAvailable(false);
        Page<Item> resultElement = itemService.search(0, 1, 0, newItem.getName());
        assertThat(resultElement.getTotalElements(), equalTo(0L));
    }

    @Test
    void deleteItem() {
        Item newItem = before();
        itemService.deleteItem(newItem.getOwner().getId(), newItem.getId());
        try {
            itemService.getItemById(newItem.getOwner().getId(), newItem.getId());
        } catch (ItemNotFoundException e) {
            assertThat(e.getClass(), equalTo(ItemNotFoundException.class));
        }
    }

    @Test
    void getEnrichResponse() {
        Item newItem = beforeWithComment();
        ItemResponseDto resultElement = itemService.enrichResponse(newItem, newItem.getOwner().getId(), LocalDateTime.now());
        assertThat(resultElement.getComments().size(), equalTo(1));
    }
}






