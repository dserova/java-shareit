package ru.practicum.shareit.request.dto;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-item-requests.
 */
public class ItemRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}