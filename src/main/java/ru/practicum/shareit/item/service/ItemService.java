package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long id, ItemDto itemDto);

    ItemDto updateItem(Long id, ItemDto itemDto, Long itemId);

    ItemBookingDto getItemDtoById(Long userId, Long id);
    Item getItemById(Long itemId);

    List<ItemBookingDto> getAllItemsDtoOfUser(Long id);
    List<Item> getAllItemsOfUser(Long id);

    List<ItemDto> searchItemsByNameOrDescription(String text);

    CommentDto createComment(Long id, Long itemId, CommentDto commentDto);
}
