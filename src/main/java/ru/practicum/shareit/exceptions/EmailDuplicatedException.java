package ru.practicum.shareit.exceptions;

public class EmailDuplicatedException extends RuntimeException{
    public EmailDuplicatedException(String message) {
        super(message);
    }
}
