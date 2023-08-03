package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.error.ItemBadRequestExcetion;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Data
@Valid
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking", schema = "Public")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @JoinColumn(name = "item_id")
    @NonNull
    @ManyToOne
    private Item item;
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User booker;
    @NonNull
    @Column(name = "booking_start")
    private LocalDateTime start;
    @NonNull
    @Column(name = "booking_end")
    private LocalDateTime end;

    public void approve(Boolean approved) {
        if (this.status.equals(Status.APPROVED)) {
            throw new ItemBadRequestExcetion();
        }
        this.status = approved ? Status.APPROVED : Status.REJECTED;
    }

    public void validStartEnd() {
        if (this.getEnd().isBefore(LocalDateTime.now())) {
            throw new ItemBadRequestExcetion();
        }
        if (this.getStart().isBefore(LocalDateTime.now())) {
            throw new ItemBadRequestExcetion();
        }
        if (this.getEnd().isBefore(this.getStart())) {
            throw new ItemBadRequestExcetion();
        }
        if (this.getEnd().isEqual(this.getStart())) {
            throw new ItemBadRequestExcetion();
        }
    }

    public void checkIsOwner(long userId) {
        this.item.checkIsOwner(userId);
    }

    public void checkIsNotOwner(long userId) {
        this.item.checkIsNotOwner(userId);
    }
}