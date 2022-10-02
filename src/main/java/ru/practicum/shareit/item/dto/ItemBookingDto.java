package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemBookingDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @Size(max = 200)
    @NotBlank(groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;
    private Long request;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    List<CommentDto> comments;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookingDto {
        Long id;
        LocalDateTime start;
        LocalDateTime end;
        Long bookerId;
    }
}
