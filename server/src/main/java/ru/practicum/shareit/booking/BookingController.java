package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.IncorrectStateException;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponseDto createNewBooking(@RequestHeader("X-Sharer-User-Id") Long id,
                                               @RequestBody BookingDto bookingDto) {
        //checkDates(bookingDto);
        return bookingService.createNewBooking(bookingDto, id);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long id,
                                             @PathVariable Long bookingId,
                                             @RequestParam Boolean approved) {
        return bookingService.approveBooking(id, bookingId, approved);
    }

    @GetMapping
    public List<BookingResponseDto> getAllBookingsByUserId(
            @RequestHeader("X-Sharer-User-Id") Long id,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "start"));
        try {
            BookingState bookingState = BookingState.valueOf(state);
            return bookingService.getAllBookingsByUserId(id, bookingState, pageRequest);
        } catch (IllegalArgumentException e) {
            throw new IncorrectStateException("Unknown state: " + state);
        }
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long id,
                                             @PathVariable Long bookingId) {
        return bookingService.getBookingById(id, bookingId);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllBookingsOfCurrentUserItems(
            @RequestHeader("X-Sharer-User-Id") Long id,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "start"));
        try {
            BookingState bookingState = BookingState.valueOf(state);
            return bookingService.getAllBookingsOfCurrentUserItems(id, bookingState, pageRequest);
        } catch (IllegalArgumentException e) {
            throw new IncorrectStateException("Unknown state: " + state);
        }
    }
}
