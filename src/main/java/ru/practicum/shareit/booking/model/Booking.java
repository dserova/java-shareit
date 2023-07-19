package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Valid
@Builder
public class Booking {
    private int id;
    @NonNull
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NonNull
    @NotBlank
    private Status status;
    private Item item;
    private User booker;
    private LocalDateTime start;
    private LocalDateTime end;
}