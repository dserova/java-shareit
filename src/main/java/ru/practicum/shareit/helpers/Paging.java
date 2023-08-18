package ru.practicum.shareit.helpers;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.error.PageableBadRequestExcetion;


@NoArgsConstructor
public class Paging {
    public Pageable getPageable(int start, int pageSize) {
        if (start < 0) {
            throw new PageableBadRequestExcetion();
        }
        if (pageSize <= 0) {
            throw new PageableBadRequestExcetion();
        }

        return PageRequest.of(start / pageSize, pageSize);
    }

    public Pageable getPageable(int start, int pageSize, String sortField) {
        if (start < 0) {
            throw new PageableBadRequestExcetion();
        }
        if (pageSize <= 0) {
            throw new PageableBadRequestExcetion();
        }
        return PageRequest.of(start / pageSize, pageSize, Sort.by(sortField).descending());
    }
}
