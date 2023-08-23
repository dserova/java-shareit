package ru.practicum.shareit.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.helpers.Generate;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CommentRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private CommentRepository repository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    public void verifyManager() {
        Assertions.assertNotNull(em);
    }

    @Test
    void verifySaveComment() {
        Comment comment = new Generate().randomNullId(Comment.class);
        Assertions.assertEquals(0, repository.count());
        Assertions.assertEquals(0, comment.getId());
        comment.setAuthor(userRepository.save(comment.getAuthor()));
        comment.getItem().setOwner(userRepository.save(comment.getItem().getOwner()));
        comment.getItem().getRequest().setRequestor(userRepository.save(comment.getItem().getRequest().getRequestor()));
        comment.getItem().setRequest(itemRequestRepository.save(comment.getItem().getRequest()));
        comment.setItem(itemRepository.save(comment.getItem()));
        repository.save(comment);
        Assertions.assertNotEquals(0, comment.getId());
        Assertions.assertEquals(1, repository.count());
    }
}