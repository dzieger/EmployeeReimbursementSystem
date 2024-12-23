package com.revature.controllers;

import com.revature.annotations.RequiresRole;
import com.revature.models.DTOs.IncomingUserDTO;
import com.revature.models.DTOs.OutgoingUserDTO;
import com.revature.models.User;
import com.revature.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // TODO: Update Password -- PATCH --
    @PatchMapping ("/userId/{userId}")
    public ResponseEntity<User> changePassword(@PathVariable int userId,@RequestBody String password) {
        return ResponseEntity.ok(userService.updatePassword(userId, password));
    }

    /******************************* MANAGER'S ONLY *********************************/

    // TODO: Get all users -- GET --
    @GetMapping("/all")
    @RequiresRole("MANAGER")
    public ResponseEntity<List<User>> getAllUsers(HttpSession session) {
        session.getAttribute("userId");
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .header("X-Response", "No users found in DB")
                    .build();
        }
        return ResponseEntity.ok(users);
    }

    // TODO: Delete a user -- DELETE --
    @DeleteMapping("/userId/{delUserId}/delete")
    @RequiresRole("MANAGER")
    public ResponseEntity<String> deleteUserById(@PathVariable int delUserId, HttpSession session) {
        int userId = (int) session.getAttribute("userId");
        if (userService.findById(delUserId).getUserId() == userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot delete your own account");
        }
        return ResponseEntity.ok(userService.deleteUser(delUserId, userId));
    }

    // TODO: Update an employee's role to Manager -- PATCH --
    @PatchMapping("/userId/{promUserId}/promote")
    @RequiresRole("MANAGER")
    public ResponseEntity<OutgoingUserDTO> promoteUserToManager(@PathVariable int promUserId, HttpSession session) {
        int userId = (int) session.getAttribute("userId");
        if (userService.findById(promUserId).getUserId() == userId) {
            throw new IllegalArgumentException("You cannot promote yourself to Manager");
        }
        return ResponseEntity.ok(userService.promoteUserToManager(promUserId));
    }


}