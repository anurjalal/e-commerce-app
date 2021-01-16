package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.example.demo.utils.DataDummy.dummyUser;
import static com.example.demo.utils.DataDummy.dummyUserReq;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    private static CreateUserRequest dummyUserReq = dummyUserReq();

    private static User dummyUser = dummyUser();

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(encoder.encode(dummyUserReq.getPassword())).thenReturn("convertedToHashedPassword");

        ResponseEntity<User> response = userController.createUser(dummyUserReq);
        User respondAsUser = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(respondAsUser);
        assertEquals(0L, respondAsUser.getId());
        assertEquals(dummyUserReq.getUsername(), respondAsUser.getUsername());
        assertEquals("convertedToHashedPassword", respondAsUser.getPassword());
    }

    @Test
    public void verify_user_not_found_with_search_by_id() throws Exception {
        when(encoder.encode(dummyUserReq.getPassword())).thenReturn("convertedToHashedPassword");
        User savedUser = dummyUser();
        savedUser.setPassword("convertedToHashedPassword");
        when(userRepo.findById(0L)).thenReturn(Optional.of(savedUser));

        ResponseEntity<User> response = userController.findById(30L);
        User respondAsUser = response.getBody();

        assertNotEquals(200, response.getStatusCodeValue());
        assertNull(respondAsUser);
    }

    @Test
    public void verify_found_with_search_by_username() throws Exception {
        when(encoder.encode(dummyUserReq.getPassword())).thenReturn("convertedToHashedPassword");
        User savedUser = dummyUser;
        savedUser.setPassword("convertedToHashedPassword");
        when(userRepo.findByUsername(dummyUserReq.getUsername())).thenReturn(savedUser);

        ResponseEntity<User> response = userController.findByUserName(dummyUserReq.getUsername());
        User respondAsUser = response.getBody();

        assertNotNull(respondAsUser);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0L, respondAsUser.getId());
        assertEquals(dummyUserReq.getUsername(), respondAsUser.getUsername());
        assertEquals(savedUser.getPassword(), respondAsUser.getPassword());
    }

}
