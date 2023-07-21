package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/*
 * TODO: in future sprint
 */

@Data
public class Booking {
    private int id;
    private String name;
    private String description;
    private Status status;
    private Item item;
    private User booker;
    private LocalDateTime start;
    private LocalDateTime end;
}