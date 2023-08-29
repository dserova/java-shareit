package ru.practicum.shareit.request.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "requests", schema = "Public")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private long id;
    @NonNull
    @Column(name = "description")
    private String description;
    @NonNull
    @JoinColumn(name = "user_id")
    @OneToOne
    private User requestor;
    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();
}