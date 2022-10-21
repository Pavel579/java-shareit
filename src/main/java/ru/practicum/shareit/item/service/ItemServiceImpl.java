package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.Utils.getNullPropertyNames;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final ItemMapper mapper;

    @Override
    @Transactional
    public ItemDto createItem(Long id, ItemDto itemDto) {
        checkId(id);
        User user = userService.getUserById(id);
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() != null) {
            itemRequest = itemRequestService.getItemRequestById(itemDto.getRequestId());
        }
        Item item = repository.save(mapper.mapToItem(itemDto, user, itemRequest));
        return mapper.mapToItemDto(item);
    }

    @Override
    @Transactional
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
                itemId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start")));
        ItemBookingDto.BookingDto nextBookingDto = mapper.mapToLastNextBookingDto(repository.findNextBooking(userId,
                itemId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start")));
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
    public List<ItemBookingDto> getAllItemsDtoOfUser(Long id, PageRequest pageRequest) {
        List<Item> items = getAllItemsOfUser(id, pageRequest);
        List<ItemBookingDto> result = new ArrayList<>();
        for (Item item : items) {
            ItemBookingDto.BookingDto lastBookingDto = mapper.mapToLastNextBookingDto(repository.findLastBooking(id,
                    item.getId(), LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start")));
            ItemBookingDto.BookingDto nextBookingDto = mapper.mapToLastNextBookingDto(repository.findNextBooking(id,
                    item.getId(), LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start")));
            List<Comment> comments = commentRepository.findComments(item.getId());
            result.add(mapper.mapToItemBookingDto(item, lastBookingDto, nextBookingDto,
                    mapper.mapToListCommentDto(comments)));
        }
        return result.stream().sorted(Comparator.comparing(ItemBookingDto::getId)).collect(Collectors.toList());
    }

    @Override
    public List<Item> getAllItemsOfUser(Long id, PageRequest pageRequest) {
        checkId(id);
        return repository.findByOwnerId(id, pageRequest);
    }

    @Override
    public List<ItemDto> searchItemsByNameOrDescription(String text, PageRequest pageRequest) {
        if (!text.equals("")) {
            return mapper.mapToListItemDto(repository.searchItemsByNameOrDescription(text, pageRequest));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
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
