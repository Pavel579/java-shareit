package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_Id(Long userId, Pageable pageable);

    List<Booking> findAllByItem_Owner_Id(Long userId, Pageable pageable);

    List<Booking> findByBooker_IdAndEndBefore(Long userId, LocalDateTime now, Pageable pageable);

    List<Booking> findByBooker_IdAndStartAfter(Long userId, LocalDateTime now, Pageable pageable);

    List<Booking> findByBooker_IdAndStatus(Long userId, BookingStatus waiting, Pageable pageable);

    List<Booking> findByBooker_IdAndStartBeforeAndEndAfter(Long userId, LocalDateTime now, LocalDateTime now1, Pageable pageable);

    List<Booking> findByItem_Owner_IdAndStartBeforeAndEndAfter(Long userId, LocalDateTime now, LocalDateTime now1, Pageable pageable);

    List<Booking> findByItem_Owner_IdAndEndBefore(Long userId, LocalDateTime now, Pageable pageable);

    List<Booking> findByItem_Owner_IdAndStartAfter(Long userId, LocalDateTime now, Pageable pageable);

    List<Booking> findByItem_Owner_IdAndStatus(Long userId, BookingStatus waiting, Pageable pageable);

    @Query("select b from Booking b where b.booker.id = ?1 and b.item.id = ?2 and b.end < ?3")
    Booking findBooking(Long id, Long itemId, LocalDateTime now);
}
