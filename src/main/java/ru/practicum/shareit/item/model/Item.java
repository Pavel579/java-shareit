package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Cloneable {
    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
    private Long owner;
    private ItemRequest request;

    @Override
    public Item clone() throws CloneNotSupportedException {
        return (Item) super.clone();
    }
}
