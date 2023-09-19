package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.helpers.Generate;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserRequestDto;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(classes = ShareItGateway.class)
public class UserTest {

    private final UserController userController;

    @MockBean
    UserClient userClient;

    @Test
    void createUserFail1() {
        try {
            userController.createUser(new UserRequestDto());
            throw new RuntimeException();
        } catch (ConstraintViolationException e) {
            assertThat(e.getClass(), equalTo(ConstraintViolationException.class));
        }
    }

    @Test
    void createUserFail2() {
        try {
            UserRequestDto user = new Generate().random(UserRequestDto.class);
            user.setEmail("---");
            userController.createUser(user);
            throw new RuntimeException();
        } catch (ConstraintViolationException e) {
            assertThat(e.getClass(), equalTo(ConstraintViolationException.class));
        }
    }

    @Test
    void createUser() {
        when(userClient.createUser(any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        UserRequestDto user = new Generate().random(UserRequestDto.class);
        ResponseEntity<Object> res = userController.createUser(user);
        Assertions.assertEquals(res.getStatusCode().value(), 200);
    }

    @Test
    void updateUser() {
        when(userClient.updateUser(anyLong(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        UserRequestDto user = new Generate().random(UserRequestDto.class);
        ResponseEntity<Object> res = userController.updateUser(user.getId(), user);
        Assertions.assertEquals(res.getStatusCode().value(), 200);
    }

    @Test
    void getUser() {
        when(userClient.getUserById(anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        UserRequestDto user = new Generate().random(UserRequestDto.class);
        ResponseEntity<Object> res = userController.getUserById(user.getId());
        Assertions.assertEquals(res.getStatusCode().value(), 200);
    }

    @Test
    void getAllUser() {
        when(userClient.getAllUsers())
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<Object> res = userController.getAllUsers();
        Assertions.assertEquals(res.getStatusCode().value(), 200);
    }

    @Test
    void deleteUser() {
        when(userClient.deleteUser(anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        UserRequestDto user = new Generate().random(UserRequestDto.class);
        ResponseEntity<Object> res = userController.deleteUser(user.getId());
        Assertions.assertEquals(res.getStatusCode().value(), 200);
    }
}






