package ru.practicum.shareit.comment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
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
@Table(name = "comment", schema = "Public")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long id;
    @NotBlank
    @Column(name = "text")
    private String text;
    @JoinColumn(name = "item_id")
    @ManyToOne
    private Item item;
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User author;
    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();
}