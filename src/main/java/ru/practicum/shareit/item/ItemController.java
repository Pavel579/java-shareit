package ru.practicum.shareit.item;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.utils.Create;
import ru.practicum.shareit.utils.Update;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long id,
                              @Validated(Create.class) @RequestBody ItemDto itemDto) {
        return itemMapper.mapToItemDto(itemService.createItem(id, itemMapper.mapToItem(itemDto, id)));
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable Long id) {
        return itemMapper.mapToItemDto(itemService.getItemById(id));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long id,
                              @Validated(Update.class) @RequestBody ItemDto itemDto,
                              @PathVariable Long itemId) {
        return itemMapper.mapToItemDto(itemService.updateItem(id, itemDto, itemId));
    }

    @GetMapping
    public List<ItemDto> getAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long id) {
        return itemMapper.mapToListItemDto(itemService.getAllItemsOfUser(id));
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByNameOrDescription(@RequestParam String text) {
        return itemMapper.mapToListItemDto(itemService.searchItemsByNameOrDescription(text));
    }
}
