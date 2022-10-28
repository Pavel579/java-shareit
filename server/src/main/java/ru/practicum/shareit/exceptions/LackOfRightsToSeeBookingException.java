package ru.practicum.shareit.exceptions;

public class LackOfRightsToSeeBookingException extends RuntimeException {
    public LackOfRightsToSeeBookingException(String message) {
        super(message);
    }
}
