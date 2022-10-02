package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long userId);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long userId, LocalDateTime now, LocalDateTime now1);

    List<Booking> findByBookerIdAndEndBefore(Long userId, LocalDateTime now);

    List<Booking> findByBookerIdAndStartAfter(Long userId, LocalDateTime now);

    List<Booking> findByBookerIdAndStatus(Long userId, BookingStatus waiting);

    List<Booking> findAllById(Long userId);

    @Query("select b from Booking b where b.booker.id = ?1")
    List<Booking> findAllByUserId(Long userId);

    List<Booking> findAllByBooker_Id(Long userId);
    List<Booking> findAllByItem_Owner_Id(Long userId);

    List<Booking> findByBooker_IdAndEndBefore(Long userId, LocalDateTime now);

    List<Booking> findByBooker_IdAndStartAfter(Long userId, LocalDateTime now);

    List<Booking> findByBooker_IdAndStatus(Long userId, BookingStatus waiting);

    List<Booking> findByBooker_IdAndStartBeforeAndEndAfter(Long userId, LocalDateTime now, LocalDateTime now1);

    List<Booking> findByItem_Owner_IdAndStartBeforeAndEndAfter(Long userId, LocalDateTime now, LocalDateTime now1);

    List<Booking> findByItem_Owner_IdAndEndBefore(Long userId, LocalDateTime now);

    List<Booking> findByItem_Owner_IdAndStartAfter(Long userId, LocalDateTime now);

    List<Booking> findByItem_Owner_IdAndStatus(Long userId, BookingStatus waiting);

    @Query("select b from Booking b where b.booker.id = ?1 and b.item.id = ?2 and b.end < ?3")
    Booking findBooking(Long id, Long itemId, LocalDateTime now);
}
