package ru.practicum.shareit.comment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDto {
    private long id;
    @NotBlank
    private String text;
    private String authorName;
    private LocalDateTime created;
}
