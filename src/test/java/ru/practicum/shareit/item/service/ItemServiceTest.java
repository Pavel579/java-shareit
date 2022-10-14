package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.IncorrectIdException;
import ru.practicum.shareit.exceptions.IncorrectOwnerException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemServiceTest {
    private final ItemMapper mapper = new ItemMapper();
    private ItemServiceImpl itemService;
    private ItemRepository itemRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;
    private ItemRequestService itemRequestService;
    private UserService userService;

    private Item item;
    private Item updatedItem;
    private User user;
    private ItemDto itemDto;
    private ItemRequest itemRequest;
    private Booking booking;

    @BeforeEach
    void beforeEach() {
        itemRepository = mock(ItemRepository.class);
        bookingRepository = mock(BookingRepository.class);
        commentRepository = mock(CommentRepository.class);
        itemRequestService = mock(ItemRequestService.class);
        userService = mock(UserService.class);
        itemService = new ItemServiceImpl(itemRepository, bookingRepository, commentRepository,
                itemRequestService, userService, mapper);
        user = new User(1L, "asdf", "asdfd@mail.ru");
        itemRequest = new ItemRequest(1L, "adsfasd", 1L, LocalDateTime.now());
        itemDto = new ItemDto(1L, "item1", "description", true, null);
        item = new Item(1L, "item1", "description", true, user, null);
        updatedItem = new Item(1L, "item updated", "description", true, user, null);
        booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now(), item, user, BookingStatus.APPROVED);
    }

    @Test
    void createItemTest() {
        when(userService.getUserById(1L)).thenReturn(user);
        when(itemRepository.save(item)).thenReturn(item);
        ItemDto result = itemService.createItem(1L, itemDto);

        assertNotNull(result);
        assertEquals(result.getName(), "item1");
    }

    @Test
    void updateItemTest() {
        when(userService.getUserById(1L)).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
        when(itemRepository.save(updatedItem)).thenReturn(updatedItem);
        ItemDto result = itemService.updateItem(1L, itemDto, 1L);

        assertNotNull(result);
        assertEquals(result.getName(), "item updated");
    }

    @Test
    void getItemDtoByIdTest() {
        when(itemRepository.findLastBooking(1L, 1L, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start")))
                .thenReturn(Collections.singletonList(new Booking(1L, LocalDateTime.now(), LocalDateTime.now(), item, user, BookingStatus.APPROVED)));
        when(itemRepository.findNextBooking(1L, 1L, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start")))
                .thenReturn(Collections.singletonList(new Booking(2L, LocalDateTime.now(), LocalDateTime.now(), item, user, BookingStatus.APPROVED)));
        when(commentRepository.findComments(1L)).thenReturn(Collections.emptyList());
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
        ItemBookingDto result = itemService.getItemDtoById(1L, 1L);

        assertNotNull(result);
        assertEquals(result.getName(), "item1");
    }

    @Test
    void getAllItemsDtoOfUserTest() {
        when(itemRepository.findLastBooking(1L, 1L, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start")))
                .thenReturn(Collections.singletonList(new Booking(1L, LocalDateTime.now(), LocalDateTime.now(), item, user, BookingStatus.APPROVED)));
        when(itemRepository.findNextBooking(1L, 1L, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start")))
                .thenReturn(Collections.singletonList(new Booking(2L, LocalDateTime.now(), LocalDateTime.now(), item, user, BookingStatus.APPROVED)));
        when(commentRepository.findComments(1L)).thenReturn(Collections.emptyList());
        when(itemRepository.findByOwnerId(1L, PageRequest.of(0, 2))).thenReturn(Collections.singletonList(item));
        List<ItemBookingDto> result = itemService.getAllItemsDtoOfUser(1L, PageRequest.of(0, 2));

        assertNotNull(result);
        assertEquals(result.get(0).getName(), "item1");
    }

    @Test
    void searchItemsByNameOrDescriptionWithEmptyTextTest() {
        List<ItemDto> result = itemService.searchItemsByNameOrDescription("", PageRequest.of(0, 2));
        assertTrue(result.isEmpty());
    }

    @Test
    void searchItemsByNameOrDescriptionWithTextTest() {
        when(itemRepository.searchItemsByNameOrDescription("qwerty", PageRequest.of(0, 2)))
                .thenReturn(Collections.singletonList(item));
        List<ItemDto> result = itemService.searchItemsByNameOrDescription("qwerty", PageRequest.of(0, 2));
        assertNotNull(result);
        assertEquals(result.get(0).getName(), "item1");
    }

    @Test
    void createCommentTest() {
        when(bookingRepository.findBooking(any(), any(), any()))
                .thenReturn(booking);
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
        when(userService.getUserById(1L)).thenReturn(user);
        when(commentRepository.save(any())).thenReturn(new Comment(1L, "comment1", item, user, LocalDateTime.now()));
        CommentDto dto = new CommentDto(1L, "comment1", "Pavel", LocalDateTime.now());
        CommentDto result = itemService.createComment(1L, 1L, dto);

        assertNotNull(result);
        assertEquals(result.getText(), "comment1");
    }

    @Test
    void checkIdTest() {
        assertThrows(IncorrectIdException.class, () -> itemService.createItem(-1L, itemDto));
    }

    @Test
    void itemNotFoundExceptionTest() {
        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(10L));
    }

    @Test
    void incorrectOwnerExceptionTest() {
        when(userService.getUserById(1L)).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));

        assertThrows(IncorrectOwnerException.class, () -> itemService.updateItem(2L, itemDto, 1L));
    }
}
