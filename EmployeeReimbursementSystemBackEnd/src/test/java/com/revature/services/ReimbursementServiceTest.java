package com.revature.services;

import com.revature.exceptions.ReimbursementNotFoundException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.DTOs.OutgoingReimbursementDTO;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.repositories.ReimbursementHistoryRepository;
import com.revature.repositories.ReimbursementRepository;
import com.revature.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReimbursementServiceTest {

    @InjectMocks
    private ReimbursementService reimbursementService;

    @Mock
    private ReimbursementRepository reimbursementRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReimbursementHistoryRepository reimbursementHistoryRepository;

    @Test
    void testCreateReimbursement() {
        int userId = 1;
        User mockUser = new User();
        mockUser.setUserId(userId);
        mockUser.setUsername("testUser");
        mockUser.setPassword("testPassword");
        mockUser.setFirstName("Test");
        mockUser.setLastName("User");

        Reimbursement mockReimbursement = new Reimbursement();
        mockReimbursement.setDescription("Test reimbursement");
        mockReimbursement.setAmount(100.0);

        Reimbursement savedReimbursement = new Reimbursement();
        savedReimbursement.setDescription("Test reimbursement");
        savedReimbursement.setAmount(100.0);
        savedReimbursement.setUser(mockUser);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(reimbursementRepository.save(mockReimbursement)).thenReturn(savedReimbursement);

        // Act
        OutgoingReimbursementDTO result = reimbursementService.createReimbursement(mockReimbursement, userId);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getReimbId());
        Assertions.assertEquals("Test reimbursement", result.getDescription());
        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verify(reimbursementRepository, Mockito.times(1)).save(mockReimbursement);
    }

    @Test
    void testCreateReimbursement_userNotFound() {
        int userId = 1;
        User mockUser = new User();
        mockUser.setUserId(userId);
        mockUser.setUsername("testUser");
        mockUser.setPassword("testPassword");
        mockUser.setFirstName("Test");
        mockUser.setLastName("User");

        Reimbursement mockReimbursement = new Reimbursement();
        mockReimbursement.setDescription("Test reimbursement");

        Mockito.when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        // Act
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            reimbursementService.createReimbursement(mockReimbursement, userId);
        });

        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verify(reimbursementRepository, Mockito.never()).save(mockReimbursement);
    }

    @Test
    void updateReimbursementTest() {
        int reimbId = 1;
        String updatedDescription = "Updated description";

        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setUsername("testUser");
        mockUser.setPassword("testPassword");
        mockUser.setFirstName("Test");
        mockUser.setLastName("User");

        Reimbursement mockReimbursement = new Reimbursement();
        mockReimbursement.setReimbId(reimbId);
        mockReimbursement.setDescription("Test");
        mockReimbursement.setAmount(250);
        mockReimbursement.setUser(mockUser);

        Reimbursement updatedReimbursement = new Reimbursement();
        updatedReimbursement.setReimbId(reimbId);
        updatedReimbursement.setDescription(updatedDescription);
        updatedReimbursement.setAmount(250);
        updatedReimbursement.setUser(mockUser);

        Mockito.when(reimbursementRepository.findById(reimbId)).thenReturn(Optional.of(mockReimbursement));
        Mockito.when(reimbursementRepository.save(mockReimbursement)).thenReturn(updatedReimbursement);

        Reimbursement result = reimbursementService.updateDescription(reimbId, updatedDescription);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(updatedDescription, result.getDescription());
        Mockito.verify(reimbursementRepository, Mockito.times(1)).findById(reimbId);
        Mockito.verify(reimbursementRepository, Mockito.times(1)).save(mockReimbursement);

    }

    void updateReimbursementTest_reimbursementNotFound() {
        int reimbId = 1;
        String updatedDescription = "Updated description";

        Mockito.when(reimbursementRepository.findById(reimbId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ReimbursementNotFoundException.class, () -> {
            reimbursementService.updateDescription(reimbId, updatedDescription);
        });

        Mockito.verify(reimbursementRepository, Mockito.times(1)).findById(reimbId);
        Mockito.verify(reimbursementRepository, Mockito.never()).save(Mockito.any());
    }
}
