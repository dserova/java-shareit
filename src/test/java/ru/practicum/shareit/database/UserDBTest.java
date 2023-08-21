package ru.practicum.shareit.database;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.helpers.Generate;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserDBTest {
    private final UserService userService;

    private final User user = new Generate().random(User.class);

    private final UserRequestDto userUpdate = new Generate().random(UserRequestDto.class);

    User before() {
        return userService.createUser(user);
    }

    @Test
    void createUser() {
        User resultElement = userService.getUserById(before().getId());
        assertThat(resultElement.getId(), notNullValue());
        assertThat(resultElement.getName(), equalTo(user.getName()));
        assertThat(resultElement.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void getAllUser() {
        before();
        List<User> resultElement = userService.getAllUsers();
        assertThat(resultElement.get(0).getId(), notNullValue());
        assertThat(resultElement.get(0).getName(), equalTo(user.getName()));
        assertThat(resultElement.get(0).getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void getUser() {
        User resultElement = userService.getUserById(before().getId());
        assertThat(resultElement.getId(), notNullValue());
        assertThat(resultElement.getName(), equalTo(user.getName()));
        assertThat(resultElement.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void updateUser() {
        userService.updateUser(before().getId(), userUpdate);
        List<User> resultElement = userService.getAllUsers();
        assertThat(resultElement.get(0).getId(), notNullValue());
        assertThat(resultElement.get(0).getName(), equalTo(userUpdate.getName()));
        assertThat(resultElement.get(0).getEmail(), equalTo(userUpdate.getEmail()));
    }
}






