package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.DatesAreNotCorrectException;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    BookingRepository repository;
    UserService userService;
    ItemService itemService;
    BookingMapper mapper;

    public BookingServiceImpl(BookingRepository repository, UserService userService,
                              ItemService itemService, BookingMapper mapper) {
        this.repository = repository;
        this.userService = userService;
        this.itemService = itemService;
        this.mapper = mapper;
    }

    @Override
    public BookingDto createNewBooking(BookingDto bookingDto, Long userId) {
        checkDates(bookingDto);
        User user = userService.getUserById(userId);
        Item item = itemService.getItemById(bookingDto.getItemId());
        if(item.getOwner().getId().equals(userId)){
            throw new ItemNotFoundException("Это вещь пользователя");
        }
        if (item.getAvailable()) {
            Booking booking = repository.save(mapper.mapToBooking(bookingDto, item, user));
            return mapper.mapToBookingDto(booking);
        } else {
            throw new ItemIsNotAvailableException("Item not available");
        }
    }

    /*@Override
    public BookingOwnerDto approveBooking(Long userId, Long bookingId, Boolean isApproved) {
        userService.getUserById(userId);
        Booking booking = repository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException("Booking не найден"));
        Item item = itemService.getItemById(booking.getItemId());
        if (item.getOwner().equals(userId)) {
            if (isApproved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
            repository.save(booking);
            return mapper.mapToBookingOwnerDto(booking, item);
        } else {
            throw new LackOfRightsToSeeBookingException("Нет прав для просмотра booking");
        }
    }*/

    @Override
    public BookingGetDto approveBooking(Long userId, Long bookingId, Boolean isApproved) {
        Booking booking = repository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException("Booking не найден"));
        Item item = itemService.getItemById(booking.getItem().getId());
        userService.getUserById(userId);
        if (item.getOwner().getId().equals(userId)) {
            if (isApproved) {
                if (!booking.getStatus().equals(BookingStatus.APPROVED)){
                    booking.setStatus(BookingStatus.APPROVED);
                }else {
                    throw new IncorrectStateException("Already Approved");
                }
            } else {
                if (!booking.getStatus().equals(BookingStatus.REJECTED)){
                    booking.setStatus(BookingStatus.REJECTED);
                }else{
                    throw new IncorrectStateException("Already Rejected");
                }
            }
            repository.save(booking);
            return mapper.mapToBookingGetDto(booking);
        } else {
            throw new LackOfRightsToSeeBookingException("Нет прав для просмотра booking");
        }
    }

    @Override
    public BookingGetDto getBookingById(Long userId, Long bookingId) {
        userService.getUserById(userId);
        Booking booking = repository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException("Booking не найден"));
        Item item = itemService.getItemById(booking.getItem().getId());
        if (item.getOwner().getId().equals(userId) || booking.getBooker().getId().equals(userId)) {
            return mapper.mapToBookingGetDto(booking);
        } else {
            throw new LackOfRightsToSeeBookingException("Нет прав для просмотра booking");
        }
    }

    @Override
    public List<BookingGetDto> getAllBookingsByUserId(Long userId, String state) {
        userService.getUserById(userId);
        List<Booking> bookings;
        if (state != null) {
            switch (state) {
                case "ALL":
                    bookings = repository.findAllByBooker_Id(userId);
                    break;
                case "CURRENT":
                    bookings = repository.findByBooker_IdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(), LocalDateTime.now());
                    break;
                case "PAST":
                    bookings = repository.findByBooker_IdAndEndBefore(userId, LocalDateTime.now());
                    break;
                case "FUTURE":
                    bookings = repository.findByBooker_IdAndStartAfter(userId, LocalDateTime.now());
                    break;
                case "WAITING":
                    bookings = repository.findByBooker_IdAndStatus(userId, BookingStatus.WAITING);
                    break;
                case "REJECTED":
                    bookings = repository.findByBooker_IdAndStatus(userId, BookingStatus.REJECTED);
                    break;
                default:
                    throw new IncorrectStateException("Unknown state: " + state);
            }
        } else {
            bookings = repository.findAllByBooker_Id(userId);
        }

        return mapper.mapToListBookingGetDto(bookings.stream()
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .collect(Collectors.toList()));
    }

    /*@Override
    public List<BookingTestDto> getAllBookingsByUserId(Long userId) {
        userService.getUserById(userId);
        List<Booking> bookings = new ArrayList<>();
        //List<Item> items = new ArrayList<>();
        Map<Booking, Item> bookingsGetMap = new HashMap<>();
        switch (state) {
            case "ALL":
                bookings = repository.findByBookerId(userId);
                break;
            case "CURRENT":
                bookings = repository.findByBookerIdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case "**PAST**":
                bookings = repository.findByBookerIdAndEndBefore(userId, LocalDateTime.now());
                break;
            case "FUTURE":
                bookings = repository.findByBookerIdAndStartAfter(userId, LocalDateTime.now());
                break;
            case "WAITING":
                bookings = repository.findByBookerIdAndStatus(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = repository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED);
                break;
            default:
                break;
        }
        for (Booking b : bookings) {
            bookingsGetMap.put(b, itemService.getItemById(b.getItemId()));
        }
        return mapper.mapToListBookingGetDto(bookingsGetMap).stream()
                .sorted(Comparator.comparing(BookingGetDto::getStart))
                .collect(Collectors.toList());
    }*/


    @Override
    public List<BookingGetDto> getAllBookingsOfCurrentUserItems(Long userId, String state) {
        userService.getUserById(userId);
        List<Booking> bookings;
        if (state != null) {
            switch (state) {
                case "ALL":
                    bookings = repository.findAllByItem_Owner_Id(userId);
                    break;
                case "CURRENT":
                    bookings = repository.findByItem_Owner_IdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(), LocalDateTime.now());
                    break;
                case "PAST":
                    bookings = repository.findByItem_Owner_IdAndEndBefore(userId, LocalDateTime.now());
                    break;
                case "FUTURE":
                    bookings = repository.findByItem_Owner_IdAndStartAfter(userId, LocalDateTime.now());
                    break;
                case "WAITING":
                    bookings = repository.findByItem_Owner_IdAndStatus(userId, BookingStatus.WAITING);
                    break;
                case "REJECTED":
                    bookings = repository.findByItem_Owner_IdAndStatus(userId, BookingStatus.REJECTED);
                    break;
                default:
                    throw new IncorrectStateException("Unknown state: " + state);
            }
        } else {
            bookings = repository.findAllByItem_Owner_Id(userId);
        }
        return mapper.mapToListBookingGetDto(bookings.stream()
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .collect(Collectors.toList()));
    }

    private void checkDates(BookingDto booking) {
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new DatesAreNotCorrectException("Дата начала после даты окончания");
        }
    }
}
