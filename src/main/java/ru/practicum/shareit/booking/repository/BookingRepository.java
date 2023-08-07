package ru.practicum.shareit.booking.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<List<Booking>> findByBooker_IdOrderByStartDesc(long bookerId);

    Optional<List<Booking>> findByBooker_IdAndStartAfterOrderByStartDesc(long bookerId, @NonNull LocalDateTime start);

    Optional<List<Booking>> findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(long bookerId, @NonNull LocalDateTime start, @NonNull LocalDateTime end);

    Optional<List<Booking>> findByBooker_IdAndEndBeforeOrderByStartDesc(long bookerId, @NonNull LocalDateTime end);

    Optional<List<Booking>> findByBooker_IdAndStatusOrderByStartDesc(long bookerId, Status status);

    Optional<List<Booking>> findByItem_Owner_IdOrderByStartDesc(long itemOwnerId);

    Optional<List<Booking>> findByItem_Owner_IdAndStartAfterOrderByStartDesc(long itemOwnerId, @NonNull LocalDateTime start);

    Optional<List<Booking>> findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(long itemOwnerId, @NonNull LocalDateTime start, @NonNull LocalDateTime end);

    Optional<List<Booking>> findByItem_Owner_IdAndEndBeforeOrderByStartDesc(long itemOwnerId, @NonNull LocalDateTime end);

    Optional<List<Booking>> findByItem_Owner_IdAndStatusOrderByStartDesc(long itemOwnerId, Status status);

    Optional<Booking> findFirstByBooker_IdAndEndBeforeOrderByStartDesc(long bookerId, @NonNull LocalDateTime end);

    Optional<Booking> findFirstByItem_IdAndItem_Owner_IdAndStartAfterAndStatusOrderByStartAsc(long itemId, long itemOwnerId, @NonNull LocalDateTime start, Status status);

    Optional<Booking> findFirstByItem_IdAndItem_Owner_IdAndStartBeforeAndStatusOrderByStartDesc(long itemId, long itemOwnerId, @NonNull LocalDateTime end, Status status);

    Optional<Booking> findByIdAndBooker_IdOrIdAndItem_Owner_Id(long id, long bookerId, long id2, long itemOwnerId);
}
