package com.revature.initializer;

import com.revature.models.User;
import com.revature.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(AppInitializer.class);

    @Autowired
    public AppInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User(0, "admin", "admin", "ADMIN", "password", "MANAGER");
            logger.info("No users found, admin account created");
            userRepository.save(admin);
        }
    }

}
