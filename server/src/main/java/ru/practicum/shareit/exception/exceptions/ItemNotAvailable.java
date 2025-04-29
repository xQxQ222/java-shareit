package ru.practicum.shareit.exception.exceptions;

public class ItemNotAvailable extends RuntimeException {
    public ItemNotAvailable(String message) {
        super(message);
    }
}
