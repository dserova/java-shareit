package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Getter
@Setter
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
}