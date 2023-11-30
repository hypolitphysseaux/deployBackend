package com.example.backend.loan_request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequest,Integer> {

    // Check loan request
    boolean existsByBook_IdAndUser_Email(int book_id, String user_email);

    // Find relevant loan requests
    List<LoanRequest> findLoanRequestsByBook_IdAndStatus(int book_id,String status);

    // Delete loan requests with a condition
    void deleteLoanRequestsByUser_Email(String user_email);
    void deleteLoanRequestsByBook_Owner(String owner_email);
    void deleteLoanRequestsByBook_Id(int book_id);

}
