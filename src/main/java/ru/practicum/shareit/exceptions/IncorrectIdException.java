package ru.practicum.shareit.exceptions;

public class IncorrectIdException extends RuntimeException{
    public IncorrectIdException(String message) {
        super(message);
    }
}
