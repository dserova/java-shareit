package ru.practicum.shareit.item.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<List<Item>> findByOwner_IdOrderById(long ownerId);

    Optional<Page<Item>> findByOwner_IdOrderById(long ownerId, Pageable pageable);

    Optional<Item> findByIdAndOwner_Id(long id, long ownerId);

    List<Item> findByRequest_Id(long requestId);

    @Query("select i from Item i " +
            "where " +
            "i.available=TRUE " +
            "and " +
            "trim(:text)<>'' " +
            "and " +
            "upper(concat(i.name, ',,,', i.description)) like upper(concat('%', :text, '%'))")
    Optional<Page<Item>> search(@NonNull @Param("text") String text, Pageable pageable);

    @Query("select i from Item i " +
            "where " +
            "i.available=TRUE " +
            "and " +
            "trim(:text)<>'' " +
            "and " +
            "upper(concat(i.name, ',,,', i.description)) like upper(concat('%', :text, '%'))")
    Optional<List<Item>> search(@NonNull @Param("text") String text);
}
