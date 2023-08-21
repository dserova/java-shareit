package ru.practicum.shareit.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.helpers.Generate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ItemRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void verifyManager() {
        Assertions.assertNotNull(em);
    }

    @Test
    void verifySaveItem() {
        Item item = new Generate().randomNullId(Item.class);
        Assertions.assertEquals(0, repository.count());
        Assertions.assertEquals(0, item.getId());
        item.setOwner(userRepository.save(item.getOwner()));
        item.setRequest(null);
        repository.save(item);
        Assertions.assertNotEquals(0, item.getId());
        Assertions.assertEquals(1, repository.count());
    }

    @Test
    void verifyDeleteItem() {
        Item item = new Generate().randomNullId(Item.class);
        Assertions.assertEquals(0, repository.count());
        Assertions.assertEquals(0, item.getId());
        item.setOwner(userRepository.save(item.getOwner()));
        item.setRequest(null);
        repository.save(item);
        repository.delete(item);
        Assertions.assertEquals(0, repository.count());
    }

    @Test
    void verifySearchByName() {
        Item item = new Generate().randomNullId(Item.class);
        Assertions.assertEquals(0, repository.count());
        Assertions.assertEquals(0, item.getId());
        item.setOwner(userRepository.save(item.getOwner()));
        item.setRequest(null);
        repository.save(item);
        List<Item> list = repository.search(item.getName().toUpperCase()).orElseThrow();
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void verifySearchByDescription() {
        Item item = new Generate().randomNullId(Item.class);
        Assertions.assertEquals(0, repository.count());
        Assertions.assertEquals(0, item.getId());
        item.setOwner(userRepository.save(item.getOwner()));
        item.setRequest(null);
        repository.save(item);
        List<Item> list = repository.search(item.getDescription().toUpperCase()).orElseThrow();
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void verifySearchFail() {
        Item item = new Generate().randomNullId(Item.class);
        Assertions.assertEquals(0, repository.count());
        Assertions.assertEquals(0, item.getId());
        item.setOwner(userRepository.save(item.getOwner()));
        item.setRequest(null);
        repository.save(item);
        List<Item> list = repository.search(item.getName().toUpperCase() + "@@@").orElseThrow();
        Assertions.assertEquals(0, list.size());
    }
}