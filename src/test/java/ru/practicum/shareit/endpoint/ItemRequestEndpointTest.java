package ru.practicum.shareit.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.shareit.error.ItemBadRequestExcetion;
import ru.practicum.shareit.error.ItemNotFoundException;
import ru.practicum.shareit.error.ItemRequestBadRequestExcetion;
import ru.practicum.shareit.error.ItemRequestNotFoundException;
import ru.practicum.shareit.helpers.Generate;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestEndpointTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService service;

    @Autowired
    private MockMvc mvc;


    private final ItemRequest itemRequest = new Generate().random(ItemRequest.class);

    private final ItemRequestRequestDto request = new ModelMapper().map(itemRequest, ItemRequestRequestDto.class);

    @Test
    void createItem() throws Exception {
        when(service.createItemRequest(anyLong(), any()))
                .thenReturn(itemRequest);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription())));
    }

    @Test
    void getAllItem() throws Exception {
        List<ItemRequestResponseWithItemsDto> itemRequests = new ArrayList<>();
        itemRequests.add(mapper.convertValue(itemRequest, ItemRequestResponseWithItemsDto.class));

        when(service.getAllItemRequests(anyLong()))
                .thenReturn(itemRequests);

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$[0].id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequest.getDescription())));
    }

    @Test
    void getAllItemByPage() throws Exception {
        List<ItemRequestResponseWithItemsDto> itemRequests = new ArrayList<>();
        itemRequests.add(mapper.convertValue(itemRequest, ItemRequestResponseWithItemsDto.class));

        when(service.getAllItemRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(itemRequests);

        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$[0].id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequest.getDescription())));
    }

    @Test
    void getItem() throws Exception {
        when(service.getItemRequestById(anyLong(), anyLong()))
                .thenReturn(mapper.convertValue(itemRequest, ItemRequestResponseWithItemsDto.class));

        mvc.perform(get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription())));
    }

    @Test
    void error1() throws Exception {
        when(service.getItemRequestById(anyLong(), anyLong()))
                .thenThrow(new ItemRequestBadRequestExcetion());

        mvc.perform(patch("/requests/999")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 999))
                .andExpect(status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void error2() throws Exception {
        when(service.getItemRequestById(anyLong(), anyLong()))
                .thenThrow(new ItemRequestNotFoundException());

        mvc.perform(patch("/requests/999")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 999))
                .andExpect(status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

}
