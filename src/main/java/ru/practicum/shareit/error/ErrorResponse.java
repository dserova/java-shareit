package ru.practicum.shareit.error;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ErrorResponse {

    private List<ErrorItem> errors = new ArrayList<>();

    public void addError(ErrorItem error) {
        this.errors.add(error);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}