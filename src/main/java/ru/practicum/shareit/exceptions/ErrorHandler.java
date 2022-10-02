package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity<String> handleEmailNotFoundException(final EmailNotFoundException e) {
        log.debug("EmailNotFoundException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleEmailDuplicatedException(final EmailDuplicatedException e) {
        log.debug("EmailDuplicatedException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIncorrectIdException(final IncorrectIdException e) {
        log.debug("IncorrectIdException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleUserNotFoundException(final UserNotFoundException e) {
        log.debug("UserNotFoundException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleItemNotFoundException(final ItemNotFoundException e) {
        log.debug("ItemNotFoundException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIncorrectOwnerException(final IncorrectOwnerException e) {
        log.debug("IncorrectOwnerException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleOtherException(final Throwable e) {
        log.debug("OtherException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.debug("MethodArgumentNotValidException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleBookingNotFoundException(final BookingNotFoundException e) {
        log.debug("BookingNotFoundException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleLackOfRightsToSeeBookingException(final LackOfRightsToSeeBookingException e) {
        log.debug("LackOfRightsToSeeBookingException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleItemIsNotAvailableException(final ItemIsNotAvailableException e) {
        log.debug("ItemIsNotAvailableException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleDatesAreNotCorrectException(final DatesAreNotCorrectException e) {
        log.debug("DatesAreNotCorrectException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleIncorrectStateException(final IncorrectStateException e) {
        log.debug("IncorrectStateException");
        return new ResponseEntity<>(Map.of("error",e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
