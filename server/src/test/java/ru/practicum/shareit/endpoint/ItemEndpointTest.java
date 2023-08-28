package ru.practicum.shareit.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.error.CommentNotFoundException;
import ru.practicum.shareit.error.ItemBadRequestException;
import ru.practicum.shareit.error.ItemNotFoundException;
import ru.practicum.shareit.error.PageableBadRequestException;
import ru.practicum.shareit.helpers.Generate;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemController.class)
class ItemEndpointTest {
    private final Item item = new Generate().random(Item.class);

    private final ItemRequestDto request = new ModelMapper().map(item, ItemRequestDto.class);

    private final Comment comment = new Generate().random(Comment.class);

    private final CommentRequestDto requestComment = new ModelMapper().map(comment, CommentRequestDto.class);

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService service;

    @MockBean
    CommentService commentService;

    @Autowired
    private MockMvc mvc;

    @Test
    void createItem() throws Exception {
        when(service.createItem(anyLong(), any(), anyLong()))
                .thenReturn(item);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())));
    }

    @Test
    void updateItem() throws Exception {
        when(service.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(item);
        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())));

    }

    @Test
    void updateItemFail() throws Exception {
        when(service.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(item);
        mvc.perform(put("/items/3")
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAllItem() throws Exception {
        List<Item> items = new ArrayList<>();
        items.add(item);
        Page<Item> pageItems = new PageImpl<>(items);
        when(service.getAllItems(anyInt(), anyInt(), anyLong()))
                .thenReturn(pageItems);
        when(service.enrichResponse(any(), anyLong(), any()))
                .thenReturn(mapper.convertValue(item, ItemResponseDto.class));
        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(item.getName())));
    }

    @Test
    void getItem() throws Exception {
        when(service.getItemById(anyLong(), anyLong()))
                .thenReturn(item);
        when(service.enrichResponse(any(), anyLong(), any()))
                .thenReturn(mapper.convertValue(item, ItemResponseDto.class));
        mvc.perform(get("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())));
    }

    @Test
    void deleteItem() throws Exception {
        doNothing().when(service).deleteItem(anyLong(), anyLong());
        mvc.perform(delete("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.ALL)
                        .accept(MediaType.ALL)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createComment() throws Exception {
        when(commentService.createComment(anyLong(), anyLong(), any()))
                .thenReturn(comment);
        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(requestComment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(comment.getText())));
    }

    @Test
    void error1() throws Exception {
        when(service.updateItem(anyLong(), anyLong(), any()))
                .thenThrow(new ItemBadRequestException());
        mvc.perform(patch("/items/999")
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 999))
                .andExpect(status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void error2() throws Exception {
        when(service.updateItem(anyLong(), anyLong(), any()))
                .thenThrow(new ItemNotFoundException());
        mvc.perform(patch("/items/999")
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 999))
                .andExpect(status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void error3() throws Exception {
        when(commentService.createComment(anyLong(), anyLong(), any()))
                .thenThrow(new CommentNotFoundException());
        mvc.perform(patch("/items/999/comment")
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 999))
                .andExpect(status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void error4() throws Exception {
        when(service.getAllItems(anyInt(), anyInt(), anyLong()))
                .thenThrow(new PageableBadRequestException());
        when(service.enrichResponse(any(), anyLong(), any()))
                .thenReturn(mapper.convertValue(item, ItemResponseDto.class));
        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }
}
