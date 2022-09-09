package ru.practicum.shareit.item.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.IncorrectIdException;
import ru.practicum.shareit.exceptions.IncorrectOwnerException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.utils.Utils.getNullPropertyNames;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage inMemoryItemStorage;
    private final ItemMapper itemMapper;
    private final UserService userService;

    public ItemServiceImpl(ItemStorage inMemoryItemStorage, ItemMapper itemMapper, UserService userService) {
        this.inMemoryItemStorage = inMemoryItemStorage;
        this.itemMapper = itemMapper;
        this.userService = userService;
    }

    @Override
    public Item createItem(Long id, ItemDto itemDto) {
        checkId(id);
        userService.getUserById(id);
        return inMemoryItemStorage.createItem(itemMapper.mapToItem(itemDto, id));
    }

    @Override
    public Item updateItem(Long id, ItemDto itemDto, Long itemId) throws CloneNotSupportedException {
        checkId(id);
        Item itemFromStorage = getItemById(itemId).clone();
        if (itemFromStorage.getOwner().equals(id)) {
            BeanUtils.copyProperties(itemDto, itemFromStorage, getNullPropertyNames(itemDto));
            return inMemoryItemStorage.updateItem(itemFromStorage);
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
            return new ArrayList<>();
        }
    }

    private void checkId(Long id) {
        if (id == null || id <= 0) {
            throw new IncorrectIdException(String.format("Id <%s> некорректен", id));
        }
    }
}
