package ru.practicum.shareit.item.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwner(User owner);

    List<Item> findDistinctByDescriptionContainsIgnoreCaseAndAvailableOrNameContainsIgnoreCaseAndAvailable(@NonNull @NotBlank String description, @NonNull Boolean available, @NonNull @NotBlank String name, @NonNull Boolean available2);
}
