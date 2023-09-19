package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.helpers.Generate;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemRequestCreateDto;
import ru.practicum.shareit.item.dto.ItemRequestUpdateDto;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(classes = ShareItGateway.class)
public class ItemTest {

    private final ItemController itemController;

    @MockBean
    ItemClient itemClient;

    @Test
    void createItem() {
        when(itemClient.createItem(anyLong(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        ItemRequestCreateDto item = new Generate().random(ItemRequestCreateDto.class);
        ResponseEntity<Object> res = itemController.createItem(1, item);
        Assertions.assertEquals(res.getStatusCode().value(), 200);
    }

    @Test
    void updateItem() {
        when(itemClient.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        ItemRequestUpdateDto item = new Generate().random(ItemRequestUpdateDto.class);
        ResponseEntity<Object> res = itemController.updateItem(1, 1, item);
        Assertions.assertEquals(res.getStatusCode().value(), 200);
    }

    @Test
    void getItem() {
        when(itemClient.getItemById(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        ItemRequestCreateDto item = new Generate().random(ItemRequestCreateDto.class);
        ResponseEntity<Object> res = itemController.getItemById(1, item.getId());
        Assertions.assertEquals(res.getStatusCode().value(), 200);
    }

    @Test
    void getAllItems() {
        when(itemClient.getAllItems(anyLong(), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<Object> res = itemController.getAllItems(0, 1, 1);
        Assertions.assertEquals(res.getStatusCode().value(), 200);
    }

    @Test
    void getAllItemsFail() {
        try {
            itemController.getAllItems(1, 0, 0);
            throw new RuntimeException();
        } catch (ConstraintViolationException e) {
            assertThat(e.getClass(), equalTo(ConstraintViolationException.class));
        }
    }

    @Test
    void deleteItem() {
        when(itemClient.deleteItem(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        ItemRequestCreateDto item = new Generate().random(ItemRequestCreateDto.class);
        ResponseEntity<Object> res = itemController.deleteItem(1, item.getId());
        Assertions.assertEquals(res.getStatusCode().value(), 200);
    }
}






