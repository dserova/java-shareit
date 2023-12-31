package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
public class ItemResponseDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private BookingRequestDto lastBooking;
    private BookingRequestDto nextBooking;
    private List<CommentResponseDto> comments;
}