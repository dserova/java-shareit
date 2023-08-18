package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemResponseShortDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingResponseDto {
    private long id;
    private Status status;
    @NonNull
    private ItemResponseShortDto item;
    @NonNull
    private User booker;
    private LocalDateTime start;
    private LocalDateTime end;
}
