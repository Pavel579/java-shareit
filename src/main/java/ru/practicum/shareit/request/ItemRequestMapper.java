package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Component
public class ItemRequestMapper {
    public ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto, Long requesterId) {
        return new ItemRequest(itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                requesterId,
                itemRequestDto.getCreated());
    }

    public ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated());
    }

    public ItemRequestResponseDto mapToItemRequestResponseDto(ItemRequest itemRequest, List<ItemDto> itemDtoList) {
        return new ItemRequestResponseDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                itemDtoList);
    }
}
