package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    private Long id;
    private String description;
    private Long requestor;
    private LocalDateTime created;
}
