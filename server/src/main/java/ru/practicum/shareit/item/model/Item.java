package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "items", schema = "Public", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private long id;
    @NonNull
    @Column(name = "name")
    private String name;
    @NonNull
    @Column(name = "description")
    private String description;
    @NonNull
    @Column(name = "available")
    private Boolean available;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;
    @OneToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}
