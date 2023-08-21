package ru.practicum.shareit.database;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.helpers.Generate;
import ru.practicum.shareit.item.dto.ItemResponseShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestResponseWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemRequestDBTest {
    private final UserService userService;

    private final ItemRequestService itemRequestService;

    private final ItemService itemService;

    private final Item item = new Generate().random(Item.class);

    Item before() {
        User nu = userService.createUser(item.getOwner());
        User nru = userService.createUser(item.getRequest().getRequestor());
        ItemRequest nir = itemRequestService.createItemRequest(nru.getId(), item.getRequest());
        return itemService.createItem(nu.getId(), item, nir.getId());
    }

    @Test
    void getAllItemRequests() {
        Item newItem = before();
        List<ItemResponseShortDto> list = new ArrayList<>();
        list.add(new ModelMapper().map(newItem, ItemResponseShortDto.class));
        List<ItemRequestResponseWithItemsDto> resultElement = itemRequestService.getAllItemRequests(newItem.getOwner().getId(), 0, 1);
        assertThat(resultElement.get(0).getId(), notNullValue());
        assertThat(resultElement.get(0).getItems(), equalTo(list));
        assertThat(resultElement.get(0).getDescription(), equalTo(item.getRequest().getDescription()));
    }

    @Test
    void getAllItemRequestsFail() {
        Item newItem = before();
        List<ItemRequestResponseWithItemsDto> resultElement = itemRequestService.getAllItemRequests(newItem.getRequest().getRequestor().getId(), 0, 1);
        assertThat(resultElement.size(), equalTo(0));
    }

    @Test
    void getItemRequest() {
        Item newItem = before();
        List<ItemResponseShortDto> list = new ArrayList<>();
        list.add(new ModelMapper().map(newItem, ItemResponseShortDto.class));
        ItemRequestResponseWithItemsDto resultElement = itemRequestService.getItemRequestById(newItem.getRequest().getId(), newItem.getRequest().getRequestor().getId());
        assertThat(resultElement.getId(), notNullValue());
        assertThat(resultElement.getItems(), equalTo(list));
        assertThat(resultElement.getDescription(), equalTo(item.getRequest().getDescription()));
    }
}






