package ru.practicum.shareit.booking.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<List<Booking>> findByBooker_IdOrderByStartDesc(long booker_id);

    Optional<List<Booking>> findByBooker_IdAndStartAfterOrderByStartDesc(long booker_id, @NonNull LocalDateTime start);

    Optional<List<Booking>> findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(long booker_id, @NonNull LocalDateTime start, @NonNull LocalDateTime end);

    Optional<List<Booking>> findByBooker_IdAndEndBeforeOrderByStartDesc(long booker_id, @NonNull LocalDateTime end);

    Optional<List<Booking>> findByBooker_IdAndStatusOrderByStartDesc(long booker_id, Status status);

    Optional<List<Booking>> findByItem_Owner_IdOrderByStartDesc(long item_owner_id);

    Optional<List<Booking>> findByItem_Owner_IdAndStartAfterOrderByStartDesc(long item_owner_id, @NonNull LocalDateTime start);

    Optional<List<Booking>> findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(long item_owner_id, @NonNull LocalDateTime start, @NonNull LocalDateTime end);

    Optional<List<Booking>> findByItem_Owner_IdAndEndBeforeOrderByStartDesc(long item_owner_id, @NonNull LocalDateTime end);

    Optional<List<Booking>> findByItem_Owner_IdAndStatusOrderByStartDesc(long item_owner_id, Status status);

    Optional<Booking> findFirstByBooker_IdAndEndBeforeOrderByStartDesc(long booker_id, @NonNull LocalDateTime end);

    Optional<Booking> findFirstByItem_IdAndItem_Owner_IdAndStartAfterAndStatusOrderByStartAsc(long item_id, long item_owner_id, @NonNull LocalDateTime start, Status status);

    Optional<Booking> findFirstByItem_IdAndItem_Owner_IdAndStartBeforeAndStatusOrderByStartDesc(long item_id, long item_owner_id, @NonNull LocalDateTime end, Status status);

    Optional<Booking> findByIdAndBooker_IdOrIdAndItem_Owner_Id(long id, long booker_id, long id2, long item_owner_id);
}
