package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {

    Item createItem(Item item);

    Item updateItem(ItemDto itemDto, Item item);

    Optional<Item> getItemById(Long id);

    List<Item> getAllItemsOfUser(Long id);

    List<Item> searchItemsByNameOrDescription(String text);
}
