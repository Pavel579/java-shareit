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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
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


    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long id,
                              @Validated(Create.class) @RequestBody ItemDto itemDto) {
        return itemService.createItem(id, itemDto);
    }

    @GetMapping("/{id}")
    public ItemBookingDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        return itemService.getItemDtoById(userId, id);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long id,
                              @Validated(Update.class) @RequestBody ItemDto itemDto,
                              @PathVariable Long itemId) {
        return itemService.updateItem(id, itemDto, itemId);
    }

    @GetMapping
    public List<ItemBookingDto> getAllItemsDtoOfUser(@RequestHeader("X-Sharer-User-Id") Long id) {
        return itemService.getAllItemsDtoOfUser(id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByNameOrDescription(@RequestParam String text) {
        return itemService.searchItemsByNameOrDescription(text);
    }


    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") Long id,
                                    @PathVariable Long itemId,
                                    @Validated(Create.class) @RequestBody CommentDto commentDto) {
        return itemService.createComment(id, itemId, commentDto);
    }
}
