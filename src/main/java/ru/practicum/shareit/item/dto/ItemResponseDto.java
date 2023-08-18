package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDto {
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Boolean available;
    private Long requestId;

    private BookingRequestDto lastBooking;
    private BookingRequestDto nextBooking;

    private List<CommentResponseDto> comments;
}