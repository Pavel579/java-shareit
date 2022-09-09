package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Long id, ItemDto itemDto);

    Item updateItem(Long id, ItemDto itemDto, Long itemId) throws CloneNotSupportedException;

    Item getItemById(Long id);

    List<Item> getAllItemsOfUser(Long id);

    List<Item> searchItemsByNameOrDescription(String text);
}
