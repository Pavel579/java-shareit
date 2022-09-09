package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemStorage implements ItemStorage {
    private static Long id = 0L;
    private final Map<Long, Item> itemStorage = new HashMap<>();

    @Override
    public Item createItem(Item item) {
        item.setId(setIdToItem());
        itemStorage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        itemStorage.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        return Optional.ofNullable(itemStorage.get(id));
    }

    @Override
    public List<Item> getAllItemsOfUser(Long id) {
        return new ArrayList<>(itemStorage.values()).stream()
                .filter(item -> item.getOwner().equals(id))
                .collect(Collectors.toList());
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
}
