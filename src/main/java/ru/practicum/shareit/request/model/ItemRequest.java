package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@Valid
@NoArgsConstructor
@Entity
@Table(name = "requests", schema = "Public")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private long id;
    @NonNull
    @NotBlank
    @Column(name = "description")
    private String description;
    @NonNull
    @JoinColumn(name = "user_id")
    @OneToOne
    private User requestor;
    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();
}