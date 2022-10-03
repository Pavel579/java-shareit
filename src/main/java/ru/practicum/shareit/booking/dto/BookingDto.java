package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private Long itemId;
    @FutureOrPresent
    private LocalDateTime start;
    @FutureOrPresent
    private LocalDateTime end;


}
