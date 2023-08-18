package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Valid
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "Public", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;
    @NonNull
    @NotBlank
    @Column(name = "name")
    private String name;
    @NotBlank
    @NonNull
    @Email
    @Column(name = "email")
    private String email;
}