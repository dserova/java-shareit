package ru.practicum.shareit.user.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByIdNotAndEmail(long id, @NotBlank @NonNull @Email String email);

    boolean existsByEmail(@NotBlank @NonNull @Email String email);
}
