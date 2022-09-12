package ru.practicum.shareit.item.storage;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.Utils.getNullPropertyNames;

@Repository
public class InMemoryItemStorage implements ItemStorage {
    private static Long id = 0L;
    private final Map<Long, Item> itemStorage = new HashMap<>();
    private final Map<Long, List<Item>> userItemIndex = new LinkedHashMap<>();

    @Override
    public Item createItem(Item item) {
        item.setId(setIdToItem());
        itemStorage.put(item.getId(), item);
        addUserItemIndex(item);
        return item;
    }

    @Override
    public Item updateItem(ItemDto itemDto, Item itemFromStorage) {
        BeanUtils.copyProperties(itemDto, itemFromStorage, getNullPropertyNames(itemDto));
        return itemFromStorage;
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        return Optional.ofNullable(itemStorage.get(id));
    }

    @Override
    public List<Item> getAllItemsOfUser(Long id) {
        return userItemIndex.get(id);
    }

    @Override
    public List<Item> searchItemsByNameOrDescription(String text) {
        return new ArrayList<>(itemStorage.values()).stream()
                .filter(item -> item.getAvailable() && (item.getDescription().toLowerCase().contains(text.toLowerCase())
                        || item.getName().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());
    }

    private Long setIdToItem() {
        return ++id;
    }

    private void addUserItemIndex(Item item) {
        List<Item> items = userItemIndex.computeIfAbsent(item.getOwner(), k -> new ArrayList<>());
        items.add(item);
        userItemIndex.put(item.getOwner(), items);
    }
}
