package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
public class UserRequestDto {
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
}