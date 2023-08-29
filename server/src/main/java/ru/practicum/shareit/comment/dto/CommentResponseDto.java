package ru.practicum.shareit.comment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDto {
    private long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
