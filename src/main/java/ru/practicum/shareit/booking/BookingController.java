package ru.practicum.shareit.booking;

import org.springframework.validation.annotation.Validated;
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
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    BookingService bookingService;
    BookingMapper mapper;

    public BookingController(BookingService bookingService, BookingMapper mapper) {
        this.bookingService = bookingService;
        this.mapper = mapper;
    }

    @PostMapping
    public BookingDto createNewBooking(@RequestHeader("X-Sharer-User-Id") Long id,
                                       @Validated @RequestBody BookingDto bookingDto) {
        return bookingService.createNewBooking(bookingDto, id);
    }

    @PatchMapping("/{bookingId}")
    public BookingGetDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long id,
                                        @PathVariable Long bookingId,
                                        @RequestParam Boolean approved) {
        return bookingService.approveBooking(id, bookingId, approved);
    }

    @GetMapping
    public List<BookingGetDto> getAllBookingsByUserId(@RequestHeader("X-Sharer-User-Id") Long id,
                                                      @RequestParam(required = false) String state) {
        return bookingService.getAllBookingsByUserId(id, state);
    }

    @GetMapping("/{bookingId}")
    public BookingGetDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long id,
                                        @PathVariable Long bookingId) {
        return bookingService.getBookingById(id, bookingId);
    }

    @GetMapping("/owner")
    public List<BookingGetDto> getAllBookingsOfCurrentUserItems(@RequestHeader("X-Sharer-User-Id") Long id,
                                                                @RequestParam(required = false) String state) {
        return bookingService.getAllBookingsOfCurrentUserItems(id, state);
    }
}
