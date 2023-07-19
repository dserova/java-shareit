package ru.practicum.shareit.share;

public class RegexpValidation {
    public String checkEmailPattern() {
        return "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
                + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$";
    }
}
