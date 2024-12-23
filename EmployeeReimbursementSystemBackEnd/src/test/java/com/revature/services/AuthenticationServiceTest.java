package com.revature.services;

import com.revature.contexts.AuthContext;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.DTOs.LoginDTO;
import com.revature.models.DTOs.OutgoingUserDTO;
import com.revature.models.User;
import com.revature.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthContext authContext;

    @Test
    void testAuthenticate() {

        User user = new User();
        user.setUserId(1);
        user.setUsername("testUser");
        user.setPassword("password");
        user.setRole("Employee");
        user.setFirstName("Test");
        user.setLastName("User");

        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(user);

        OutgoingUserDTO result = authenticationService.authenticate(new LoginDTO("testUser", "password"));

        Assertions.assertNotNull(result);
        Assertions.assertEquals("testUser", result.getUsername());
        Assertions.assertEquals("Employee", result.getRole());
    }

    @Test
    void testAuthenticate_InvalidPassword() {

        User user = new User();
        user.setUserId(1);
        user.setUsername("testUser");
        user.setPassword("password");
        user.setRole("Employee");
        user.setFirstName("Test");
        user.setLastName("User");

        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(user);

        Assertions.assertThrows(SecurityException.class, () -> {
            authenticationService.authenticate(new LoginDTO("testUser", "wrongPassword"));
        });
    }

    @Test
    void testAuthenticate_UserNotFound() {
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(null);

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            authenticationService.authenticate(new LoginDTO("testUser", "password"));
        });
    }


}
