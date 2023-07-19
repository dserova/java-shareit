package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@Valid
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items", schema = "Public", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private long id;
    @NonNull
    @NotBlank
    @Column(name = "name")
    private String name;
    @NonNull
    @NotBlank
    @Column(name = "description")
    private String description;
    @NonNull
    @Column(name = "available")
    private Boolean available;
    @OneToOne
    private User owner;
    @OneToOne
    private ItemRequest request;
}