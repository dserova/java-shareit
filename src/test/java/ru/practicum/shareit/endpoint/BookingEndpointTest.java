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
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.error.BookingBadRequestExcetion;
import ru.practicum.shareit.error.BookingNotFoundException;
import ru.practicum.shareit.error.FilterNotFoundException;
import ru.practicum.shareit.helpers.Generate;

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


@WebMvcTest(controllers = BookingController.class)
class BookingEndpointTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService service;

    @Autowired
    private MockMvc mvc;

    private final Booking booking = new Generate().random(Booking.class);
    private final BookingRequestDto request = new ModelMapper().map(booking, BookingRequestDto.class);

    @Test
    void createBooking() throws Exception {
        when(service.createBooking(anyLong(), anyLong(), any()))
                .thenReturn(booking);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));
    }

    @Test
    void updateBooking() throws Exception {
        when(service.updateBooking(anyLong(), anyLong(), any()))
                .thenReturn(booking);

        mvc.perform(patch("/bookings/1")
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));

    }

    @Test
    void updateBookingFail() throws Exception {
        when(service.updateBooking(anyLong(), anyLong(), any()))
                .thenReturn(booking);

        mvc.perform(put("/bookings/3")
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAllBookings() throws Exception {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        Page<Booking> pageItems = new PageImpl<>(bookings);

        when(service.getAllBookings(anyInt(), anyInt(), anyLong(), any()))
                .thenReturn(pageItems);

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(booking.getStatus().toString())));
    }

    @Test
    void getAllBookingsByOwner() throws Exception {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        Page<Booking> pageItems = new PageImpl<>(bookings);

        when(service.getAllBookingsByOwner(anyInt(), anyInt(), anyLong(), any()))
                .thenReturn(pageItems);

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(booking.getStatus().toString())));
    }

    @Test
    void getBooking() throws Exception {
        when(service.getBookingById(anyLong(), anyLong()))
                .thenReturn(booking);

        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));
    }

    @Test
    void deleteBooking() throws Exception {
        doNothing().when(service).deleteBooking(anyLong(), anyLong());

        mvc.perform(delete("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.ALL)
                        .accept(MediaType.ALL)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void error1() throws Exception {
        when(service.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenThrow(new BookingBadRequestExcetion());

        mvc.perform(patch("/bookings/999")
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
        when(service.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenThrow(new BookingNotFoundException());

        mvc.perform(patch("/bookings/999")
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
        when(service.getAllBookings(anyInt(), anyInt(), anyLong(), any()))
                .thenThrow(new FilterNotFoundException());

        mvc.perform(get("/bookings?state=TEST")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void error4() throws Exception {
        when(service.getAllBookings(anyInt(), anyInt(), anyLong(), any()))
                .thenThrow(new FilterNotFoundException("test"));

        mvc.perform(get("/bookings?state=TEST")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.ALL)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }
}
