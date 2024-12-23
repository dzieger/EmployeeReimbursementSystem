package com.revature.controllers;

import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementHistory;
import com.revature.models.User;
import com.revature.repositories.ReimbursementHistoryRepository;
import com.revature.repositories.ReimbursementRepository;
import com.revature.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@RequestMapping("/data")
public class DataPopulatorController {

    private final UserRepository userRepository;

    private final ReimbursementRepository reimbursementRepository;

    private final ReimbursementHistoryRepository reimbursementHistoryRepository;

    @Autowired
    public DataPopulatorController(UserRepository userRepository, ReimbursementRepository reimbursementRepository, ReimbursementHistoryRepository reimbursementHistoryRepository) {
        this.userRepository = userRepository;
        this.reimbursementRepository = reimbursementRepository;
        this.reimbursementHistoryRepository = reimbursementHistoryRepository;
    }

    @PostMapping("/populate")
    public ResponseEntity<String> populateDatabase() {


        if (userRepository.count() == 1) {
            User user1 = new User(0, "Trinity", "Chung", "tchung", "password", "EMPLOYEE");
            User user2 = new User(0, "Carter", "Robles", "crobles", "password", "EMPLOYEE");
            User user3 = new User(0, "Yareli", "Colon", "ycolon", "password", "EMPLOYEE");
            User user4 = new User(0, "Toby", "Salazar", "tsalazar", "password", "EMPLOYEE");
            User user5 = new User(0, "Lilliana", "Andrews", "landrews", "password", "EMPLOYEE");
            User user6 = new User(0, "Xiomara", "Holland", "xholland", "password", "EMPLOYEE");
            User user7 = new User(0, "Annalise", "Gray", "agray", "password", "EMPLOYEE");
            User user8 = new User(0, "Enzo", "Marks", "emarks", "password", "EMPLOYEE");
            User user9 = new User(0, "Carl", "Vance", "cvance", "password", "EMPLOYEE");
            User user10 = new User(0, "April", "Browning", "abrowning", "password", "EMPLOYEE");
            User testUser = new User(0, "Test", "User", "test", "password", "EMPLOYEE");

            userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, testUser));

            Reimbursement reimb1 = new Reimbursement(0, "Food", 100.00, "PENDING", user1, LocalDateTime.now());
//            populateReimbursements(reimb1);
            Reimbursement reimb2 = new Reimbursement(0, "Travel", 200.00, "PENDING", user2, LocalDateTime.now());
//            populateReimbursements(reimb2);
            Reimbursement reimb3 = new Reimbursement(0, "Lodging", 300.00, "PENDING", user3, LocalDateTime.now());
//            populateReimbursements(reimb3);
            Reimbursement reimb4 = new Reimbursement(0, "Food", 400.00, "PENDING", user4, LocalDateTime.now());
//            populateReimbursements(reimb4);
            Reimbursement reimb5 = new Reimbursement(0, "Travel", 500.00, "PENDING", user5, LocalDateTime.now());
//            populateReimbursements(reimb5);
            Reimbursement reimb6 = new Reimbursement(0, "Lodging", 600.00, "PENDING", user6, LocalDateTime.now());
//            populateReimbursements(reimb6);
            Reimbursement reimb7 = new Reimbursement(0, "Food", 700.00, "PENDING", user7, LocalDateTime.now());
//            populateReimbursements(reimb7);
            Reimbursement reimb8 = new Reimbursement(0, "Travel", 800.00, "PENDING", user8, LocalDateTime.now());
//            populateReimbursements(reimb8);
            Reimbursement reimb9 = new Reimbursement(0, "Lodging", 900.00, "PENDING", user9, LocalDateTime.now());
//            populateReimbursements(reimb9);
            Reimbursement reimb10 = new Reimbursement(0, "Food", 1000.00, "PENDING", user10, LocalDateTime.now());
//            populateReimbursements(reimb10);
            Reimbursement testReimb = new Reimbursement(0, "Test", 100.00, "PENDING", testUser, LocalDateTime.now());
//            populateReimbursements(testReimb);
            Reimbursement reimb11 = new Reimbursement(0, "Food", 100.00, "PENDING", user1, LocalDateTime.now());
//            populateReimbursements(reimb11);
            Reimbursement reimb12 = new Reimbursement(0, "Travel", 200.00, "PENDING", user2, LocalDateTime.now());
//            populateReimbursements(reimb12);
            Reimbursement reimb13 = new Reimbursement(0, "Lodging", 300.00, "PENDING", user1, LocalDateTime.now());
//            populateReimbursements(reimb13);
            Reimbursement reimb14 = new Reimbursement(0, "Food", 400.00, "PENDING", user4, LocalDateTime.now());
//            populateReimbursements(reimb14);
            Reimbursement reimb15 = new Reimbursement(0, "Travel", 500.00, "PENDING", user5, LocalDateTime.now());
//            populateReimbursements(reimb15);
            Reimbursement reimb16 = new Reimbursement(0, "Lodging", 600.00, "PENDING", user6, LocalDateTime.now());
//            populateReimbursements(reimb16);
            Reimbursement reimb17 = new Reimbursement(0, "Food", 700.00, "PENDING", user7, LocalDateTime.now());
//            populateReimbursements(reimb17);
            Reimbursement reimb18 = new Reimbursement(0, "Travel", 800.00, "PENDING", user8, LocalDateTime.now());
//            populateReimbursements(reimb18);
            Reimbursement reimb19 = new Reimbursement(0, "Lodging", 900.00, "PENDING", user9, LocalDateTime.now());
//            populateReimbursements(reimb19);
            Reimbursement reimb20 = new Reimbursement(0, "Food", 1000.00, "PENDING", user10, LocalDateTime.now());
//            populateReimbursements(reimb20);
            Reimbursement reimb21 = new Reimbursement(0, "Food", 100.00, "PENDING", user1, LocalDateTime.now());
//            populateReimbursements(reimb21);
            Reimbursement reimb22 = new Reimbursement(0, "Travel", 200.00, "PENDING", user2, LocalDateTime.now());
//            populateReimbursements(reimb22);
            Reimbursement reimb23 = new Reimbursement(0, "Lodging", 300.00, "PENDING", user3, LocalDateTime.now());
//            populateReimbursements(reimb23);
            Reimbursement reimb24 = new Reimbursement(0, "Food", 400.00, "PENDING", user4, LocalDateTime.now());
//            populateReimbursements(reimb24);
            Reimbursement reimb25 = new Reimbursement(0, "Travel", 500.00, "PENDING", user5, LocalDateTime.now());
//            populateReimbursements(reimb25);
            Reimbursement reimb26 = new Reimbursement(0, "Lodging", 600.00, "PENDING", user6, LocalDateTime.now());
//            populateReimbursements(reimb26);
            Reimbursement reimb27 = new Reimbursement(0, "Food", 700.00, "PENDING", user7, LocalDateTime.now());
//            populateReimbursements(reimb27);
            Reimbursement reimb28 = new Reimbursement(0, "Travel", 800.00, "PENDING", user8, LocalDateTime.now());
//            populateReimbursements(reimb28);
            Reimbursement reimb29 = new Reimbursement(0, "Lodging", 900.00, "PENDING", user9, LocalDateTime.now());
//            populateReimbursements(reimb29);
            Reimbursement reimb30 = new Reimbursement(0, "Food", 1000.00, "PENDING", user10, LocalDateTime.now());
//            populateReimbursements(reimb30);

            reimbursementRepository.saveAll(Arrays.asList(reimb1, reimb2, reimb3, reimb4, reimb5, reimb6, reimb7, reimb8, reimb9, reimb10,
                    reimb11, reimb12, reimb13, reimb14, reimb15, reimb16, reimb17, reimb18, reimb19, reimb20, reimb21, reimb22, reimb23,
                    reimb24, reimb25, reimb26, reimb27, reimb28, reimb29, reimb30));

            return ResponseEntity.ok("Database populated successfully");
        } else {
            return ResponseEntity.ok("Database already populated");
        }
    }
//
//    public void populateReimbursements (Reimbursement reimbursement) {
//        Reimbursement newReimbursement = reimbursementRepository.save(reimbursement);
//        ReimbursementHistory reimbursementHistory = new ReimbursementHistory();
//        reimbursementHistory.setReimbursement(newReimbursement);
//        reimbursementHistory.setUpdatedBy(reimbursement.getUser().getFirstName() + " " + reimbursement.getUser().getLastName());
//        reimbursementHistory.setFieldChanged("Reimbursement created");
//        reimbursementHistory.setTimestamp(LocalDateTime.now());
//        reimbursementHistoryRepository.save(reimbursementHistory);
//    }

}
