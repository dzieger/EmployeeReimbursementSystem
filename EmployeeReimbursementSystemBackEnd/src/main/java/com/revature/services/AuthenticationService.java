package com.revature.services;

import com.revature.contexts.AuthContext;
import com.revature.exceptions.UserNotFoundException;
import com.revature.exceptions.UsernameUnavailableException;
import com.revature.models.DTOs.IncomingPasswordChangeDTO;
import com.revature.models.DTOs.IncomingUserDTO;
import com.revature.models.DTOs.LoginDTO;
import com.revature.models.DTOs.OutgoingUserDTO;
import com.revature.models.User;
import com.revature.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final AuthContext authContext;

    @Autowired
    public AuthenticationService(UserRepository userRepository, AuthContext authContext) {
        this.userRepository = userRepository;
        this.authContext = authContext;
    }


    public OutgoingUserDTO authenticate(LoginDTO loginDTO) {
        logger.info("Authenticating user with username: " + loginDTO.getUsername());
        User user = userRepository.findByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new UserNotFoundException("Sorry, it looks like that username doesn't exist. Please try again");
        }
        if (user.getPassword().equals(loginDTO.getPassword())) {

            authContext.setUsername(user.getUsername());
            authContext.setUserRole(user.getRole());
            authContext.setUserRole(user.getRole());
            logger.info("User authenticated successfully");

            return new OutgoingUserDTO(
                    user.getUserId(),
                    user.getUsername(),
                    user.getRole(),
                    user.getFirstName(),
                    user.getLastName()
            );

        } else {
            throw new SecurityException("Invalid credentials, Login DENIED");
        }
    }

    public OutgoingUserDTO register(User user) {
        validateUser(user);
        userRepository.save(user);
        return new OutgoingUserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getRole(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    public OutgoingUserDTO updatePassword(IncomingPasswordChangeDTO user, String username) {
        User updatedUser = userRepository.findByUsername(username);
        if (updatedUser.getPassword() == user.getNewPassword()) {
            throw new IllegalArgumentException("New password cannot be the same as the old password");
        }
        validatePassword(user.getNewPassword());
        updatedUser.setPassword(user.getNewPassword());
        userRepository.save(updatedUser);
        return new OutgoingUserDTO(
                updatedUser.getUserId(),
                updatedUser.getUsername(),
                updatedUser.getRole(),
                updatedUser.getFirstName(),
                updatedUser.getLastName()
        );
    }

    public void logout() {

    }

    public String getRole(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("No user found"));
        return user.getRole();
    }

    void validateUser(User user) {
        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name field cannot be blank");
        }
        if (user.getLastName() == null || user.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name field cannot be blank");
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username field cannot be blank");
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UsernameUnavailableException("Sorry, that username is taken");
        }
        validatePassword(user.getPassword());
    }

    //checks password for valid syntax for a new user or update a password
    void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password field cannot be blank");
        }
        if (password.length()<8 || password.length()>16) {
            throw new IllegalArgumentException("Password must be between 8 and 16 characters long");
        }

        // Can validate strength of password by checking for at least 2 numbers, 2 symbols, and no repeating characters.
        int numCounter = 0;
        int symCounter = 0;

        char[] chars = password.toCharArray();

        // Checks for repeating characters
        for (int i = 1; i < chars.length; i++) {
            if (chars[i] == chars[i-1]) {
                throw new IllegalArgumentException("Password cannot contain repeating characters");
            }
        }

        //checks for at least 2 numbers
        for (char c : chars) {
            if (Character.isDigit(c)) {
                numCounter += 1;
            }
        }
        if (numCounter < 2) {
            throw new IllegalArgumentException("Password must contain at least 2 numbers");
        }

        // Checks for at least 2 symbols
        for (char c : chars) {
            if (!(Character.isLetterOrDigit(c)) && !(Character.isWhitespace(c))) {
                symCounter += 1;
            }
        }

        if (symCounter < 2) {
            throw new IllegalArgumentException("Password must countain at least 2 symbols");
        }


    }
}
