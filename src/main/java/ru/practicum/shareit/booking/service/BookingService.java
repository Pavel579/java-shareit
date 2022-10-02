package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;

import java.util.List;

public interface BookingService {
    BookingDto createNewBooking(BookingDto bookingDto, Long userId);

    BookingGetDto approveBooking(Long userId, Long bookingId, Boolean isApproved);

    BookingGetDto getBookingById(Long userId, Long bookingId);

    List<BookingGetDto> getAllBookingsByUserId(Long userId, String state);

    List<BookingGetDto> getAllBookingsOfCurrentUserItems(Long userId, String state);
}
