package ru.practicum.shareit.exception.handle;

import lombok.Getter;

public class ErrorResponce {
    @Getter
    private final String error;
    @Getter
    private final String description;

    public ErrorResponce(String error, String description) {
        this.error = error;
        this.description = description;
    }
}
