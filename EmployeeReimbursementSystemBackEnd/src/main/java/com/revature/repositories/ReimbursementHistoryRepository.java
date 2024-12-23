package com.revature.repositories;

import com.revature.models.ReimbursementHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReimbursementHistoryRepository extends JpaRepository<ReimbursementHistory, Integer> {
}
