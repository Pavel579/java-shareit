package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i where i.owner.id = ?1")
    List<Item> findByOwnerId(Long id);

    @Query("select i from Item i where i.available=true and (upper(i.name) like upper(concat('%', ?1, '%')) or " +
            "upper(i.description) like upper(concat('%', ?1, '%')))")
    List<Item> searchItemsByNameOrDescription(String text);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.item.id = ?2 and b.end<?3 order by b.start desc ")
    List<Booking> findLastBooking(Long userId, Long itemId, LocalDateTime now);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.item.id = ?2 and b.start>?3 order by b.start desc ")
    List<Booking> findNextBooking(Long userId, Long itemId, LocalDateTime now);
}
