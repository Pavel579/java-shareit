package ru.practicum.shareit.booking;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Future;
import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Booking {
    private Long id;
    private Long itemId;
    @Future
    private LocalDate date;
}
