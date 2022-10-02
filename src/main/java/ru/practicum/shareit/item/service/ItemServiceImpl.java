package ru.practicum.shareit.item.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
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
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.Utils.getNullPropertyNames;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ItemMapper mapper;

    public ItemServiceImpl(ItemRepository repository, BookingRepository bookingRepository, CommentRepository commentRepository, UserService userService, ItemMapper mapper) {
        this.repository = repository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public ItemDto createItem(Long id, ItemDto itemDto) {
        checkId(id);
        User user = userService.getUserById(id);
        Item item = repository.save(mapper.mapToItem(itemDto, user));
        return mapper.mapToItemDto(item);

    }

    @Override
    public ItemDto updateItem(Long id, ItemDto itemDto, Long itemId) {
        checkId(id);
        userService.getUserById(id);
        Item itemFromStorage = getItemById(itemId);
        if (itemFromStorage.getOwner().getId().equals(id)) {
            BeanUtils.copyProperties(itemDto, itemFromStorage, getNullPropertyNames(itemDto));
            return mapper.mapToItemDto(repository.save(itemFromStorage));
        } else {
            throw new IncorrectOwnerException("Данный пользователь не является владельцем вещи");
        }
    }

    @Override
    public ItemBookingDto getItemDtoById(Long userId, Long itemId) {
        ItemBookingDto.BookingDto lastBookingDto = mapper.mapToLastNextBookingDto(repository.findLastBooking(userId,
                itemId, LocalDateTime.now()));
        ItemBookingDto.BookingDto nextBookingDto = mapper.mapToLastNextBookingDto(repository.findNextBooking(userId,
                itemId, LocalDateTime.now()));
        List<Comment> comments = commentRepository.findComments(itemId);
        return mapper.mapToItemBookingDto(getItemById(itemId), lastBookingDto, nextBookingDto,
                mapper.mapToListCommentDto(comments));
    }

    @Override
    public Item getItemById(Long id) {
        checkId(id);
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item не найден"));
    }

    @Override
    public List<ItemBookingDto> getAllItemsDtoOfUser(Long id) {
        List<Item> items = getAllItemsOfUser(id);
        List<ItemBookingDto> result = new ArrayList<>();
        for (Item item : items) {
            ItemBookingDto.BookingDto lastBookingDto = mapper.mapToLastNextBookingDto(repository.findLastBooking(id,
                    item.getId(), LocalDateTime.now()));
            ItemBookingDto.BookingDto nextBookingDto = mapper.mapToLastNextBookingDto(repository.findNextBooking(id,
                    item.getId(), LocalDateTime.now()));
            List<Comment> comments = commentRepository.findComments(item.getId());
            result.add(mapper.mapToItemBookingDto(item, lastBookingDto, nextBookingDto,
                    mapper.mapToListCommentDto(comments)));
        }
        return result.stream().sorted(Comparator.comparing(ItemBookingDto::getId)).collect(Collectors.toList());
    }

    @Override
    public List<Item> getAllItemsOfUser(Long id) {
        checkId(id);
        return repository.findByOwnerId(id);
    }

    @Override
    public List<ItemDto> searchItemsByNameOrDescription(String text) {
        if (!text.equals("")) {
            return mapper.mapToListItemDto(repository.searchItemsByNameOrDescription(text));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public CommentDto createComment(Long id, Long itemId, CommentDto commentDto) {
        Booking booking = bookingRepository.findBooking(id, itemId, LocalDateTime.now());
        Item item = getItemById(itemId);
        User user = userService.getUserById(id);
        if (booking != null) {
            Comment comment = commentRepository.save(mapper.mapToComment(commentDto, item, user));
            return mapper.mapToCommentDto(comment);
        } else {
            throw new IncorrectIdException("comment incorrect");
        }
    }

    private void checkId(Long id) {
        if (id == null || id <= 0) {
            throw new IncorrectIdException(String.format("Id <%s> некорректен", id));
        }
    }
}
