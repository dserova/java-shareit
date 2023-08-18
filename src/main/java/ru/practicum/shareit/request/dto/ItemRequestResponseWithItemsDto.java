package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemResponseShortDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemRequestResponseWithItemsDto {
    private long id;
    private String description;
    private LocalDateTime created;
    private List<ItemResponseShortDto> items;
}

