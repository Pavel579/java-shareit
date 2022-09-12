package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.IncorrectIdException;
import ru.practicum.shareit.exceptions.IncorrectOwnerException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage inMemoryItemStorage;
    private final UserService userService;

    public ItemServiceImpl(ItemStorage inMemoryItemStorage, UserService userService) {
        this.inMemoryItemStorage = inMemoryItemStorage;
        this.userService = userService;
    }

    @Override
    public Item createItem(Long id, Item item) {
        checkId(id);
        userService.getUserById(id);
        return inMemoryItemStorage.createItem(item);
    }

    @Override
    public Item updateItem(Long id, ItemDto itemDto, Long itemId) {
        checkId(id);
        Item itemFromStorage = getItemById(itemId);
        if (itemFromStorage.getOwner().equals(id)) {
            return inMemoryItemStorage.updateItem(itemDto, itemFromStorage);
        } else {
            throw new IncorrectOwnerException("Данный пользователь не является владельцем вещи");
        }
    }

    @Override
    public Item getItemById(Long id) {
        checkId(id);
        return inMemoryItemStorage.getItemById(id).orElseThrow(() -> new ItemNotFoundException("Item не найден"));
    }

    @Override
    public List<Item> getAllItemsOfUser(Long id) {
        checkId(id);
        return inMemoryItemStorage.getAllItemsOfUser(id);
    }

    @Override
    public List<Item> searchItemsByNameOrDescription(String text) {
        if (!text.equals("")) {
            return inMemoryItemStorage.searchItemsByNameOrDescription(text);
        } else {
            return Collections.emptyList();
        }
    }

    private void checkId(Long id) {
        if (id == null || id <= 0) {
            throw new IncorrectIdException(String.format("Id <%s> некорректен", id));
        }
    }
}
