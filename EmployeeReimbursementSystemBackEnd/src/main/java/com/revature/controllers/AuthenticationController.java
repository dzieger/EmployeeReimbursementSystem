package com.revature.controllers;


import com.revature.models.DTOs.IncomingPasswordChangeDTO;
import com.revature.models.DTOs.IncomingUserDTO;
import com.revature.models.DTOs.LoginDTO;
import com.revature.models.DTOs.OutgoingUserDTO;
import com.revature.models.User;
import com.revature.services.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;
    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    // TODO: Secure login and Authentication
    @PostMapping("/login")
    public ResponseEntity<OutgoingUserDTO> login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        OutgoingUserDTO user = authenticationService.authenticate(loginDTO);

        if(user == null) {
            return ResponseEntity.status(401).body(null);
        }

        session.setAttribute("userId", user.getUserId());
        session.setAttribute("role", user.getRole());
        session.setAttribute("username", user.getUsername());
        logger.info("User logged in successfully: {}", user);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<OutgoingUserDTO> register(@RequestBody User user) {
        logger.info("Registering new user with username: {}", user.getUsername());
        OutgoingUserDTO newUser = authenticationService.register(user);
        logger.info("New user registered successfully: {}", newUser);
        return ResponseEntity.ok().body(newUser);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<OutgoingUserDTO> changePassword(@RequestBody IncomingPasswordChangeDTO user, HttpSession session) {
        String username = (String) session.getAttribute("username");
        logger.info("Changing password for user with username: {}", username);
        OutgoingUserDTO updatedUser = authenticationService.updatePassword(user, username);
        logger.info("Password changed successfully for user with username: {}", username);
        return ResponseEntity.ok().body(updatedUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        logger.info("User logged out successfully");
        return ResponseEntity.ok().body("You have been logged out");
    }
}
