package ru.practicum.shareit.item.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    // get items with User.id (owner)
    Optional<List<Item>> findByOwner_Id(long ownerId);

    // get item with Item.id and User.id (owner). Check correct User as Owner
    Optional<Item> findByIdAndOwner_Id(long id, long ownerId);

    @Query("select i from Item i " +
            "where " +
            "i.available=TRUE " +
            "and " +
            "upper(concat(i.name, ',,,', i.description)) like upper(concat('%', :text, '%'))")
    Optional<List<Item>> search(@NonNull @NotBlank @Param("text") String text);
}
