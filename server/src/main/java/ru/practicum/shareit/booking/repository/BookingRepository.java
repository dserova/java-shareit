package ru.practicum.shareit.booking.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Page<Booking>> findByBooker_IdOrderByStartDesc(long bookerId, Pageable pageable);

    Optional<Page<Booking>> findByBooker_IdAndStartAfterOrderByStartDesc(long bookerId, @NonNull LocalDateTime start, Pageable pageable);

    Optional<Page<Booking>> findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(long bookerId, @NonNull LocalDateTime start, @NonNull LocalDateTime end, Pageable pageable);

    Optional<Page<Booking>> findByBooker_IdAndEndBeforeOrderByStartDesc(long bookerId, @NonNull LocalDateTime end, Pageable pageable);

    Optional<Page<Booking>> findByBooker_IdAndStatusOrderByStartDesc(long bookerId, Status status, Pageable pageable);

    Optional<Page<Booking>> findByItem_Owner_IdOrderByStartDesc(long itemOwnerId, Pageable pageable);

    Optional<Page<Booking>> findByItem_Owner_IdAndStartAfterOrderByStartDesc(long itemOwnerId, @NonNull LocalDateTime start, Pageable pageable);

    Optional<Page<Booking>> findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(long itemOwnerId, @NonNull LocalDateTime start, @NonNull LocalDateTime end, Pageable pageable);

    Optional<Page<Booking>> findByItem_Owner_IdAndEndBeforeOrderByStartDesc(long itemOwnerId, @NonNull LocalDateTime end, Pageable pageable);

    Optional<Page<Booking>> findByItem_Owner_IdAndStatusOrderByStartDesc(long itemOwnerId, Status status, Pageable pageable);

    Optional<Booking> findFirstByBooker_IdAndEndBeforeOrderByStartDesc(long bookerId, @NonNull LocalDateTime end);

    Optional<Booking> findFirstByItem_IdAndItem_Owner_IdAndStartAfterAndStatusOrderByStartAsc(long itemId, long itemOwnerId, @NonNull LocalDateTime start, Status status);

    Optional<Booking> findFirstByItem_IdAndItem_Owner_IdAndStartBeforeAndStatusOrderByStartDesc(long itemId, long itemOwnerId, @NonNull LocalDateTime end, Status status);

    Optional<Booking> findByIdAndBooker_IdOrIdAndItem_Owner_Id(long id, long bookerId, long id2, long itemOwnerId);
}
