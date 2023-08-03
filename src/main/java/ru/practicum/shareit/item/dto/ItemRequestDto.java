package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Boolean available;
    private ItemRequest request;
}