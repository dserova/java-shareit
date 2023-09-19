package ru.practicum.shareit.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.helpers.Generate;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseShortDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseWithItemsDto;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.io.IOException;

@JsonTest
public class DtoJsonTest {

    @Autowired
    private JacksonTester<UserRequestDto> jsonUserRequestDto;

    @Autowired
    private JacksonTester<UserResponseDto> jsonUserResponseDto;

    @Autowired
    private JacksonTester<ItemRequestDto> jsonItemRequestDto;

    @Autowired
    private JacksonTester<ItemResponseDto> jsonItemResponseDto;

    @Autowired
    private JacksonTester<ItemResponseShortDto> jsonItemResponseShortDto;

    @Autowired
    private JacksonTester<ItemRequestRequestDto> jsonItemRequestRequestDto;

    @Autowired
    private JacksonTester<ItemRequestResponseDto> jsonItemRequestResponseDto;

    @Autowired
    private JacksonTester<ItemRequestResponseWithItemsDto> jsonItemRequestResponseWithItemsDto;

    @Autowired
    private JacksonTester<CommentRequestDto> jsonCommentRequestDto;

    @Autowired
    private JacksonTester<CommentResponseDto> jsonCommentResponseDto;

    @Autowired
    private JacksonTester<BookingRequestDto> jsonBookingRequestDto;

    public <T> void test(Class<T> targetClass, JacksonTester<T> json) throws IOException {
        T element = new Generate().random(targetClass);
        JsonContent<?> result = json.write(element);
        ObjectContent<T> elementReverse = json.parse(result.getJson());
        elementReverse.assertThat().isEqualTo(element);
    }

    @Test
    void testDto() throws Exception {
        test(UserRequestDto.class, jsonUserRequestDto);
        test(UserResponseDto.class, jsonUserResponseDto);
        test(ItemRequestDto.class, jsonItemRequestDto);
        test(ItemResponseDto.class, jsonItemResponseDto);
        test(ItemResponseShortDto.class, jsonItemResponseShortDto);
        test(ItemRequestRequestDto.class, jsonItemRequestRequestDto);
        test(ItemRequestResponseDto.class, jsonItemRequestResponseDto);
        test(ItemRequestResponseWithItemsDto.class, jsonItemRequestResponseWithItemsDto);
        test(CommentRequestDto.class, jsonCommentRequestDto);
        test(CommentResponseDto.class, jsonCommentResponseDto);
        test(BookingRequestDto.class, jsonBookingRequestDto);
    }
}
