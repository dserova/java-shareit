package ru.practicum.shareit.booking.dto;

import java.util.Optional;

public enum Filter {
    ALL,
    CURRENT,
    FUTURE,
    PAST,
    REJECTED,
    WAITING;

    public static Optional<Filter> from(String stringState) {
        for (Filter state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
