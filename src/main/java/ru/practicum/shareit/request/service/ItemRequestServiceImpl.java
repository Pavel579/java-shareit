package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.RequestNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper mapper;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemRequestDto createItemRequest(Long id, ItemRequestDto itemRequestDto) {
        userService.getUserById(id);
        ItemRequest itemRequest = repository.save(mapper.mapToItemRequest(itemRequestDto, id));
        return mapper.mapToItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestResponseDto> getCurrentUserItemRequests(Long id) {
        userService.getUserById(id);
        List<ItemRequestResponseDto> result = new ArrayList<>();
        List<ItemRequest> itemRequests = repository.findAllByRequester(id);
        for (ItemRequest itemRequest : itemRequests) {
            List<ItemDto> itemDtoList = itemMapper.mapToListItemDto(itemRepository.findAllItemsByRequest(itemRequest.getId()));
            result.add(mapper.mapToItemRequestResponseDto(itemRequest, itemDtoList));
        }
        return result;
    }

    @Override
    public ItemRequest getItemRequestById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RequestNotFoundException("Request nit found!!"));
    }

    @Override
    public List<ItemRequestResponseDto> getItemRequestsFromAnotherUsers(Long id, PageRequest pageRequest) {
        userService.getUserById(id);
        List<ItemRequestResponseDto> result = new ArrayList<>();
        Page<ItemRequest> itemRequests = repository.findItemRequestsFromAnotherUsers(id,
                pageRequest);
        for (ItemRequest itemRequest : itemRequests) {
            List<ItemDto> itemDtoList = itemMapper.mapToListItemDto(itemRepository.findAllItemsByRequest(itemRequest.getId()));
            result.add(mapper.mapToItemRequestResponseDto(itemRequest, itemDtoList));
        }
        return result;
    }

    @Override
    public ItemRequestResponseDto getItemRequestById(Long userId, Long requestId) {
        userService.getUserById(userId);
        List<ItemDto> itemDtoList = itemMapper.mapToListItemDto(itemRepository.findAllItemsByRequest(requestId));
        return mapper.mapToItemRequestResponseDto(getItemRequestById(requestId), itemDtoList);
    }
}
