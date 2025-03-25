package ru.practicum.shareit.exception.handle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.exceptions.AlreadyExistsException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.exception.exceptions.NotOwnerException;
import ru.practicum.shareit.exception.exceptions.ValidationException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity<String> handleAlreadyExists(final AlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotFound(final NotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotOwner(final NotOwnerException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleValidation(final ValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
