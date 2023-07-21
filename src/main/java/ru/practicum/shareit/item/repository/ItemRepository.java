package ru.practicum.shareit.item.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<List<Item>> findByOwner_Id(long ownerId);

    Optional<Item> findByIdAndOwner_Id(long id, long ownerId);

    Optional<List<Item>> findDistinctByDescriptionContainsIgnoreCaseAndAvailableOrNameContainsIgnoreCaseAndAvailable(@NonNull @NotBlank String description, @NonNull Boolean available, @NonNull @NotBlank String name, @NonNull Boolean available2);
}
