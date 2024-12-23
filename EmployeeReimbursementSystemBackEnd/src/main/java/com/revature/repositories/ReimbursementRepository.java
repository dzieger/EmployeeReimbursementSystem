package com.revature.repositories;

import com.revature.models.Reimbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReimbursementRepository extends JpaRepository<Reimbursement, Integer> {

    List<Reimbursement> findByUserUserId(int userId);

    List<Reimbursement> findByUserUserIdAndStatus(int userId, String status);

    List<Reimbursement> findByStatus(String status);

    void deleteById(int reimbId);

}
