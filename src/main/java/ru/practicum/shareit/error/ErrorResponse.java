package ru.practicum.shareit.error;

import com.google.gson.Gson;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {

    private final List<ErrorItem> errors = new ArrayList<>();

    public void addError(ErrorItem error) {
        this.errors.add(error);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
