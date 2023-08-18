package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingRequestDto {
    private long id;
    private Status status;
    private long itemId;
    private long bookerId;
    @NonNull
    private LocalDateTime start;
    @NonNull
    private LocalDateTime end;
}