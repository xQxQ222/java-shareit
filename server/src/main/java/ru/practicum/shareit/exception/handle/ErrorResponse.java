package ru.practicum.shareit.exception.handle;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {
    private final String error;
    private final String description;
    private final HttpStatus httpStatus;

    public ErrorResponse(String error, String description, HttpStatus httpStatus) {
        this.error = error;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
