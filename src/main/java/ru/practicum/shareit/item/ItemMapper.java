package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    public ItemDto mapToItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public Item mapToItem(ItemDto itemDto, Long id) {
        return new Item(null,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                id,
                null
        );
    }

    public List<ItemDto> mapToListItemDto(List<Item> items) {
        return items.stream().map(this::mapToItemDto).collect(Collectors.toList());
    }
}
