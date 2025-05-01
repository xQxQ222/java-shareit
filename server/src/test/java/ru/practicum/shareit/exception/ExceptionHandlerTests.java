package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.exception.exceptions.*;
import ru.practicum.shareit.exception.handle.ErrorHandler;
import ru.practicum.shareit.exception.handle.ErrorResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExceptionHandlerTests {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void handleNotFoundTest() {
        ResponseEntity<String> handle = errorHandler.handleNotFound(new NotFoundException("test"));
        assertEquals(HttpStatus.NOT_FOUND, handle.getStatusCode());
    }

    @Test
    void handleNotOwnerTest() {
        ErrorResponse handle = errorHandler.handleNotOwner(new NotOwnerException("test"));
        assertEquals(HttpStatus.FORBIDDEN, handle.getHttpStatus());
        assertEquals("test", handle.getDescription());
    }

    @Test
    void handleValidationTest() {
        ResponseEntity<String> handle = errorHandler.handleValidation(new ValidationException("test"));
        assertEquals(HttpStatus.BAD_REQUEST, handle.getStatusCode());
    }

    @Test
    void handleNotAvailableTest() {
        ResponseEntity<String> handle = errorHandler.handleNotAvailable(new ItemNotAvailable("test"));
        assertEquals(HttpStatus.BAD_REQUEST, handle.getStatusCode());
    }

    @Test
    void handleFalseBookerTest() {
        ResponseEntity<String> handle = errorHandler.handleFalseBooker(new FalseBookerException("test"));
        assertEquals(HttpStatus.BAD_REQUEST, handle.getStatusCode());
    }

    @Test
    void handleDuplicateEmail() {
        ErrorResponse handle = errorHandler.handleDuplicateEmail(new EmailDuplicateException("test"));
        assertEquals(HttpStatus.CONFLICT, handle.getHttpStatus());
        assertEquals("test", handle.getDescription());
    }
}
