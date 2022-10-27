package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") Long id,
                                                    @Validated(Create.class) @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.createItemRequest(id, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getCurrentUserItemRequests(@RequestHeader("X-Sharer-User-Id") Long id) {
        return itemRequestClient.getCurrentUserItemRequests(id);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestsFromAnotherUsers(
            @RequestHeader("X-Sharer-User-Id") Long id,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemRequestClient.getItemRequestsFromAnotherUsers(id, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long id,
                                                     @PathVariable Long requestId) {
        return itemRequestClient.getItemRequestById(id, requestId);
    }
}
