package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.IncorrectStateException;
import ru.practicum.shareit.exceptions.ItemIsNotAvailableException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.LackOfRightsToSeeBookingException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapper mapper;

    @Override
    @Transactional
    public BookingResponseDto createNewBooking(BookingDto bookingDto, Long userId) {
        User user = userService.getUserById(userId);
        Item item = itemService.getItemById(bookingDto.getItemId());
        if (item.getOwner().getId().equals(userId)) {
            throw new ItemNotFoundException("Это вещь пользователя");
        }
        if (item.getAvailable()) {
            Booking booking = repository.save(mapper.mapToBooking(bookingDto, item, user));
            return mapper.mapToBookingResponseDto(booking);
        } else {
            throw new ItemIsNotAvailableException("Item not available");
        }
    }

    @Override
    @Transactional
    public BookingResponseDto approveBooking(Long userId, Long bookingId, Boolean isApproved) {
        Booking booking = repository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException("Booking не найден"));
        Item item = itemService.getItemById(booking.getItem().getId());
        userService.getUserById(userId);
        if (item.getOwner().getId().equals(userId)) {
            if (isApproved) {
                if (!booking.getStatus().equals(BookingStatus.APPROVED)) {
                    booking.setStatus(BookingStatus.APPROVED);
                } else {
                    throw new IncorrectStateException("Already Approved");
                }
            } else {
                if (!booking.getStatus().equals(BookingStatus.REJECTED)) {
                    booking.setStatus(BookingStatus.REJECTED);
                } else {
                    throw new IncorrectStateException("Already Rejected");
                }
            }
            repository.save(booking);
            return mapper.mapToBookingResponseDto(booking);
        } else {
            throw new LackOfRightsToSeeBookingException("Нет прав для просмотра booking");
        }
    }

    @Override
    public BookingResponseDto getBookingById(Long userId, Long bookingId) {
        userService.getUserById(userId);
        Booking booking = repository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException("Booking не найден"));
        Item item = itemService.getItemById(booking.getItem().getId());
        if (item.getOwner().getId().equals(userId) || booking.getBooker().getId().equals(userId)) {
            return mapper.mapToBookingResponseDto(booking);
        } else {
            throw new LackOfRightsToSeeBookingException("Нет прав для просмотра booking");
        }
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByUserId(Long userId, BookingState state, PageRequest pageRequest) {
        userService.getUserById(userId);
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = repository.findAllByBooker_Id(userId, pageRequest);
                break;
            case CURRENT:
                bookings = repository.findByBooker_IdAndStartBeforeAndEndAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now(), pageRequest);
                break;
            case PAST:
                bookings = repository.findByBooker_IdAndEndBefore(userId,
                        LocalDateTime.now(), pageRequest);
                break;
            case FUTURE:
                bookings = repository.findByBooker_IdAndStartAfter(userId,
                        LocalDateTime.now(), pageRequest);
                break;
            case WAITING:
                bookings = repository.findByBooker_IdAndStatus(userId,
                        BookingStatus.WAITING, pageRequest);
                break;
            case REJECTED:
                bookings = repository.findByBooker_IdAndStatus(userId,
                        BookingStatus.REJECTED, pageRequest);
                break;
        }
        return mapper.mapToListBookingResponseDto(bookings);
    }

    @Override
    public List<BookingResponseDto> getAllBookingsOfCurrentUserItems(Long userId, BookingState state, PageRequest pageRequest) {
        userService.getUserById(userId);
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = repository.findAllByItem_Owner_Id(userId, pageRequest);
                break;
            case CURRENT:
                bookings = repository.findByItem_Owner_IdAndStartBeforeAndEndAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now(), pageRequest);
                break;
            case PAST:
                bookings = repository.findByItem_Owner_IdAndEndBefore(userId,
                        LocalDateTime.now(), pageRequest);
                break;
            case FUTURE:
                bookings = repository.findByItem_Owner_IdAndStartAfter(userId,
                        LocalDateTime.now(), pageRequest);
                break;
            case WAITING:
                bookings = repository.findByItem_Owner_IdAndStatus(userId,
                        BookingStatus.WAITING, pageRequest);
                break;
            case REJECTED:
                bookings = repository.findByItem_Owner_IdAndStatus(userId,
                        BookingStatus.REJECTED, pageRequest);
                break;
        }
        return mapper.mapToListBookingResponseDto(bookings);
    }
}
