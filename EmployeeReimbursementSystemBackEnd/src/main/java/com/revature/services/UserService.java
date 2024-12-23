package com.revature.services;

import com.revature.exceptions.UserDeleteFailedException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.exceptions.UsernameUnavailableException;
import com.revature.models.DTOs.OutgoingUserDTO;
import com.revature.models.User;
import com.revature.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User updatePassword(int userId, String password) {
        logger.info("Updating password for user with ID: " + userId);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Sorry no user found with Id: " + userId));
        validatePassword(password);
        user.setPassword(password);
        User updatedUser = userRepository.save(user);
        logger.info("Password updated successfully for user with ID: " + userId);
        return updatedUser;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // TODO: Show reimbursements deleted by result of user deletion
    @Transactional
    public String deleteUser(int delUserId, int userId) {
        logger.info("Manager with ID: {} -- Deleting user with ID: {}", userId, delUserId);
        User user = userRepository.findById(delUserId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + delUserId + " not found"));


        // Handle related entities explicitly if necessary
        userRepository.delete(user);
        entityManager.flush();
        entityManager.clear();

        if (userRepository.findById(delUserId).isPresent()) {
            throw new UserDeleteFailedException("User with ID " + delUserId + " was not deleted");
        }
        logger.info("Manager with ID: {}, successfully deleted user with ID: {}", userId, delUserId);
        return "User with ID " + delUserId + " deleted successfully.";
    }

    public User findById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }

    public OutgoingUserDTO promoteUserToManager(int userId) {
        logger.info("Promoting user with ID: " + userId + " to manager");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
        if (user.getRole().equals("MANAGER")) {
            throw new IllegalArgumentException("User is already a manager");
        }
        user.setRole("MANAGER");
        User promotedUser = userRepository.save(user);
        logger.info("User with ID: " + userId + " promoted to manager");
        return new OutgoingUserDTO(promotedUser.getUserId(),
                promotedUser.getUsername(),
                promotedUser.getRole(),
                promotedUser.getFirstName(),
                promotedUser.getLastName());
    }






    // Validates fields in the passed User Object
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
