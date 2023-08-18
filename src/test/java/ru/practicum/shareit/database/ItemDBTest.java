package ru.practicum.shareit.database;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.helpers.Generate;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

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

    ItemRequestDto itemUpdate = new Generate().random(ItemRequestDto.class);

    private final Item item = new Generate().random(Item.class);

    Item before() {
        User nu = userService.createUser(item.getOwner());
        ItemRequest nir = itemRequestService.createItemRequest(nu.getId(), item.getRequest());
        System.out.println(nu.getEmail());
        return itemService.createItem(nu.getId(), item, nir.getId());
    }

    @Test
    void createItem() {
        Item newItem = before();
        Item resultElement = itemService.getItemById(newItem.getOwner().getId(), newItem.getId());
        assertThat(resultElement.getId(), notNullValue());
        assertThat(resultElement.getName(), equalTo(item.getName()));
        assertThat(resultElement.getDescription(), equalTo(item.getDescription()));
        assertThat(resultElement.getOwner(), equalTo(item.getOwner()));
    }

    @Test
    void getAllItems() {
        Page<Item> resultElement = itemService.getAllItems(0, 1, before().getOwner().getId());
        assertThat(resultElement.getContent().get(0).getId(), notNullValue());
        assertThat(resultElement.getContent().get(0).getName(), equalTo(item.getName()));
        assertThat(resultElement.getContent().get(0).getDescription(), equalTo(item.getDescription()));
        assertThat(resultElement.getContent().get(0).getOwner(), equalTo(item.getOwner()));
    }

    @Test
    void getItem() {
        Item newItem = before();
        Item resultElement = itemService.getItemById(newItem.getOwner().getId(), newItem.getId());
        assertThat(resultElement.getId(), notNullValue());
        assertThat(resultElement.getName(), equalTo(item.getName()));
        assertThat(resultElement.getDescription(), equalTo(item.getDescription()));
        assertThat(resultElement.getOwner(), equalTo(item.getOwner()));
    }

    @Test
    void updateItem() {
        Item newItem = before();
        itemService.updateItem(newItem.getOwner().getId(), newItem.getId(), itemUpdate);
        Page<Item> resultElement = itemService.getAllItems(0, 1, newItem.getOwner().getId());
        assertThat(resultElement.getContent().get(0).getId(), notNullValue());
        assertThat(resultElement.getContent().get(0).getName(), equalTo(item.getName()));
        assertThat(resultElement.getContent().get(0).getDescription(), equalTo(item.getDescription()));
        assertThat(resultElement.getContent().get(0).getOwner(), equalTo(item.getOwner()));
    }

    @Test
    void searchItem() {
        Item newItem = before();
        newItem.setAvailable(true);
        Page<Item> resultElement = itemService.search(0, 1, 0, newItem.getName());
        assertThat(resultElement.getContent().get(0).getId(), notNullValue());
        assertThat(resultElement.getContent().get(0).getName(), equalTo(item.getName()));
        assertThat(resultElement.getContent().get(0).getDescription(), equalTo(item.getDescription()));
        assertThat(resultElement.getContent().get(0).getOwner(), equalTo(item.getOwner()));
    }

    @Test
    void searchItemFail() {
        Item newItem = before();
        newItem.setAvailable(false);
        Page<Item> resultElement = itemService.search(0, 1, 0, newItem.getName());
        assertThat(resultElement.getTotalElements(), equalTo(0L));
    }
}






