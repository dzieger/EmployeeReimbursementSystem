package com.revature.controllers;


import com.revature.annotations.RequiresRole;
import com.revature.models.DTOs.IncomingUserDTO;
import com.revature.models.DTOs.OutgoingReimbursementDTO;
import com.revature.models.DTOs.ResolveReimbursementDTO;
import com.revature.models.DTOs.UpdateReimbursementDTO;
import com.revature.models.Reimbursement;
import com.revature.services.ReimbursementService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reimbursements")
public class ReimbursementController {

    private final ReimbursementService reimbursementService;

    @Autowired
    public ReimbursementController(ReimbursementService reimbursementService) {
        this.reimbursementService = reimbursementService;
    }

    // TODO: Create reimbursement -- POST --
    @PostMapping("/userId/{userId}/create")
    public ResponseEntity<OutgoingReimbursementDTO> createReimbursement(@RequestBody Reimbursement reimbursement, HttpSession session){
        Integer sessionUserId = (Integer) session.getAttribute("userId");
        if (sessionUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("X-Response", "You must be logged in to create a reimbursement").build();
        }
        return ResponseEntity.ok(reimbursementService.createReimbursement(reimbursement, sessionUserId));
    }

    // TODO: See all reimbursements by userId -- GET --
    @GetMapping("/userId/{userId}/all")
    public ResponseEntity<List<Reimbursement>> getReimbursementByUserId(@PathVariable int userId) {
        List<Reimbursement> reimbursements = reimbursementService.findByUserId(userId);
        if (reimbursements.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).header("X-Response", "No reimbursement(s) found for user with Id: " + userId).build();
        }
        return ResponseEntity.ok(reimbursements);
    }

    // TODO: See all reimbursements by UserId AND status -- GET --
    @GetMapping("/userId/{userId}/status/{status}")
    public ResponseEntity<List<Reimbursement>> getReimbursementsByUserIdAndStatus(@PathVariable int userId, @PathVariable String status) {
        List<Reimbursement> reimbursements = reimbursementService.findByUserIdAndStatus(userId, status);
//        if (reimbursements.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).header("X-Response", "No reimbursement(s) found for user with Id: " + userId + ", and status of " + status).build();
//        }
        return ResponseEntity.ok(reimbursements);
    }

    // TODO: Update description of reimbursement -- PATCH --
    @PatchMapping("/reimbId/{reimbId}/update")
    public ResponseEntity<Reimbursement> updateDescription(@PathVariable int reimbId,@RequestBody UpdateReimbursementDTO updateReimbursementDTO) {
        return ResponseEntity.ok(reimbursementService.updateDescription(reimbId, updateReimbursementDTO.getDescription()));
    }

    //  User delete their own reimbursement ticket -- DELETE --
//    Reimbursements really shouldn't be deleted, but this is just an example.
//    @DeleteMapping("/reimbId/{reimbId}/delete")
//    @RequiresRole("MANAGER")
//    public ResponseEntity<String> deleteReimbursement(@PathVariable int reimbId, HttpSession session) {
//        int userId = (int) session.getAttribute("userId");
//        return ResponseEntity.ok(reimbursementService.deleteReimbursement(reimbId));
//    }

    /******************************* MANAGER'S ONLY *********************************/

    // TODO: See all reimbursements -- GET --
    @GetMapping("/all")
    @RequiresRole("MANAGER")
    public ResponseEntity<List<Reimbursement>> getAllReimbursements(HttpSession session) {
        int userId = (int) session.getAttribute("userId");
        List<Reimbursement> reimbursements = reimbursementService.findAllReimbursements();
        if (reimbursements.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).header("X-Response", "No reimbursement(s) found").build();
        }
        return ResponseEntity.ok(reimbursements);
    }

    // TODO: See all reimbursements by status -- GET --
    @GetMapping("/all/status/{status}")
    @RequiresRole("MANAGER")
    public ResponseEntity<List<Reimbursement>> getAllReimbursementsByStatus(@PathVariable String status, HttpSession session) {
        int userId = (int) session.getAttribute("userId");
        List<Reimbursement> reimbursements = reimbursementService.findAllReimbursementsByStatus(status);
        if (reimbursements.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).header("X-Response", "No reimbursement(s) found with status of " + status).build();
        }
        return ResponseEntity.ok(reimbursements);
    }


    // TODO: Resolve a reimbursement (set status to Approve or Deny) -- PATCH --
    @PatchMapping("/reimbId/{reimbId}/resolve")
    @RequiresRole("MANAGER")
    public ResponseEntity<Reimbursement> resolveReimbursement(@PathVariable int reimbId, @RequestBody ResolveReimbursementDTO resolveReimbursementDTO, HttpSession session) {
        int userId = (int) session.getAttribute("userId");
        String status = resolveReimbursementDTO.getStatus();
        return ResponseEntity.ok(reimbursementService.resolveReimbursement(reimbId, status, userId));
    }


    // ------------------------ Other Methods -------------------------

}
