package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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