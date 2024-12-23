package com.revature.services;

import com.revature.exceptions.ReimbursementNotFoundException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.DTOs.OutgoingReimbursementDTO;
import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementHistory;
import com.revature.models.User;
import com.revature.repositories.ReimbursementHistoryRepository;
import com.revature.repositories.ReimbursementRepository;
import com.revature.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;



@Service
public class ReimbursementService {

    private static final Logger logger = LoggerFactory.getLogger(ReimbursementService.class);

    @PersistenceContext
    private EntityManager entityManager;
    private final ReimbursementRepository reimbursementRepository;
    private final UserRepository userRepository;
    private final ReimbursementHistoryRepository reimbursementHistoryRepository;

    @Autowired
    public ReimbursementService(ReimbursementRepository reimbursementRepository, UserRepository userRepository, ReimbursementHistoryRepository reimbursementHistoryRepository) {
        this.reimbursementRepository = reimbursementRepository;
        this.userRepository = userRepository;
        this.reimbursementHistoryRepository = reimbursementHistoryRepository;
    }

    public OutgoingReimbursementDTO createReimbursement(Reimbursement reimbursement, int userId) throws UserNotFoundException {
        logger.info("Creating reimbursement for user with id: " + userId);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User account not found. Unable to create reimbursement"));

        try {
            validateReimbursement(reimbursement);
            logger.info("Reimbursement validated");
            reimbursement.setUser(user);
            Reimbursement newReimbursement = reimbursementRepository.save(reimbursement);


            OutgoingReimbursementDTO outgoingReimbursement = new OutgoingReimbursementDTO(
                    reimbursement.getReimbId(),
                    reimbursement.getDescription(),
                    reimbursement.getAmount(),
                    reimbursement.getStatus(),
                    reimbursement.getUser().getUserId(),
                    reimbursement.getReimbursementHistory()
            );

            logger.info("Reimbursement created successfully: {}", outgoingReimbursement);
            return outgoingReimbursement;

        } catch (Exception e) {
            logger.error("Failed to create reimursement for user with ID {}: {}", userId, e.getMessage());
            throw e;
        }
    }

    //returns List of reimbursements for a particular user
    public List<Reimbursement> findByUserId(int userId) throws ReimbursementNotFoundException{
        return reimbursementRepository.findByUserUserId(userId);
    }

    //Returns reimbursements by userid and status
    public List<Reimbursement> findByUserIdAndStatus(int userId, String status) {
        return reimbursementRepository.findByUserUserIdAndStatus(userId, status);
    }

    //Updates Description of a reimbursement
    public Reimbursement updateDescription(int reimbId, String description) {
        Reimbursement reimbursement = reimbursementRepository.findById(reimbId).orElseThrow(() ->
                new ReimbursementNotFoundException("No reimbursement found with Id: " + reimbId));
        logger.info("Updating description for reimbursement with id: {}, and userId: {}", reimbId, reimbursement.getUser().getUserId());

        ReimbursementHistory history = new ReimbursementHistory();
        history.setTimestamp(LocalDateTime.now());
        history.setUpdatedBy(reimbursement.getUser().getFirstName() + " " + reimbursement.getUser().getLastName());
        history.setFieldChanged("Description");
        history.setOldValue(reimbursement.getDescription());

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
        reimbursement.setDescription(description);
        Reimbursement updatedReimbursement = reimbursementRepository.save(reimbursement);
        logger.info("Reimbursement description updated successfully: {}", updatedReimbursement);

        history.setReimbursement(updatedReimbursement);
        history.setNewValue(updatedReimbursement.getDescription());
        reimbursementHistoryRepository.save(history);

        return updatedReimbursement;
    }

    // MANAGER"S ONLY -- returns all reimbursements
    public List<Reimbursement> findAllReimbursements(){
        return reimbursementRepository.findAll();
    }

    // MANAGER'S ONLY -- returns all reimbursements by status
    public List<Reimbursement> findAllReimbursementsByStatus(String status) {
        return reimbursementRepository.findByStatus(status);
    }

    // MANAGER'S ONLY -- Resolves a reimbursement
    public Reimbursement resolveReimbursement(int reimbId, String status, int userId) {
        logger.info("Manager with ID: {} - Resolving reimbursement with id: {}", userId ,reimbId);
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status cannot be null or blank");
        }
        Reimbursement reimbursement = reimbursementRepository.findById(reimbId).orElseThrow(() ->
                new ReimbursementNotFoundException("No reimbursement found with id " + reimbId));
        User manager = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(("No user found with id " + userId)));

        ReimbursementHistory history = new ReimbursementHistory();
        history.setTimestamp(LocalDateTime.now());
        history.setUpdatedBy(manager.getFirstName() + " " + manager.getLastName());
        history.setFieldChanged("Status");
        history.setOldValue(reimbursement.getStatus());

        if (status.equalsIgnoreCase("APPROVED")) {
            reimbursement.setStatus("APPROVED");
            history.setNewValue("APPROVED");
        } else if (status.equalsIgnoreCase("DENIED")) {
            reimbursement.setStatus("DENIED");
            history.setNewValue("DENIED");
        } else {
            throw new IllegalArgumentException("Status can only contain 'APPROVED' or 'DENIED'");
        }
        Reimbursement resolvedReimbursement = reimbursementRepository.save(reimbursement);
        logger.info("Reimbursement resolved successfully: {}", resolvedReimbursement);

        history.setReimbursement(resolvedReimbursement);

        reimbursementHistoryRepository.save(history);

        return resolvedReimbursement;
    }

    @Transactional
    public String deleteReimbursement(int reimbId) {
        logger.info("Deleting reimbursement with id: " + reimbId);
        Reimbursement reimbursement = reimbursementRepository.findById(reimbId).orElseThrow(() ->
                new ReimbursementNotFoundException("No reimbursement found with id " + reimbId));
        System.out.println("Found reimbursement with Id: " + reimbId);
        reimbursementRepository.deleteById(reimbId);
        entityManager.flush();
        entityManager.clear();
        if (reimbursementRepository.findById(reimbId).isPresent()) {
            System.out.println("Reimbursement still exists in the DB");
            throw new IllegalArgumentException("Reimbursement with id " + reimbId + " was not deleted");
        }
        logger.info("Reimbursement with id: {} deleted successfully", reimbId);
        return "Reimbursement with id " + reimbId + " has been deleted";
    }

    // Validates the reimbursement object for require fields
    public void validateReimbursement(Reimbursement reimbursement) {
        if (reimbursement.getDescription() == null || reimbursement.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
        if (reimbursement.getAmount() < .01f) {
            throw new IllegalArgumentException("Amount must a number greater than .01");
        }
    }
}
