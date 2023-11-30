package com.example.backend.loan_request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
public class LoanRequestController {
    private final LoanRequestService loanRequestService;

    @Autowired
    public LoanRequestController(LoanRequestService loanRequestService) {
        this.loanRequestService = loanRequestService;
    }

    // Adding new request
    @CrossOrigin(origins = "*")
    @PostMapping(path = "/addNewLoanRequest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String,Object>> addNewLoanRequest(
            @RequestParam("book_id") int book_id,
            @RequestParam("user_email") String user_email
            //@RequestParam("status") String status //Status by nemal prist, iba sa pri konstruktore nastavit na pending
    ){
        return loanRequestService.addNewLoanRequest(book_id,user_email);
    }

    // Getting user's requests
    @PostMapping(path = "/getMyRequests", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String,Object>> getMyRequests(
            @RequestParam("user_email") String user_email
    )
    {
        return loanRequestService.getMyRequests(user_email);
    }

    // Accepting existing request
    @PutMapping(path = "/acceptLoanRequest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String,Object>> acceptLoanRequest(
            @RequestParam("request_id") int request_id
    ){
        return loanRequestService.acceptLoanRequest(request_id);
    }

    // Denying existing request
    @PutMapping(path = "/denyLoanRequest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String,Object>> denyLoanRequest(
            @RequestParam("request_id") int request_id
    ){
        return loanRequestService.denyLoanRequest(request_id);
    }
}
