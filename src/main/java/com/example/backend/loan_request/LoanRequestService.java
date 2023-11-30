package com.example.backend.loan_request;

import com.example.backend.book.Book;
import com.example.backend.book.BookRepository;
import com.example.backend.message.MessageService;
import com.example.backend.user.User;
import com.example.backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Service
public class LoanRequestService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanRequestRepository loanRequestRepository;
    private final MessageService messageService;

    @Autowired
    public LoanRequestService(
            BookRepository bookRepository,
            UserRepository userRepository,
            LoanRequestRepository loanRequestRepository,
            MessageService messageService) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.loanRequestRepository = loanRequestRepository;
        this.messageService = messageService;
    }

    // Get user's requests (PENDING, I am the owner)
    // Notification calls this function each 5 seconds, so it can slow the server
    public ResponseEntity<Map<String,Object>> getMyRequests(String user_email){

        Map<String, Object> mapa = new HashMap<>();

        List<LoanRequest> allLoanRequests = loanRequestRepository.findAll();
        if (allLoanRequests.isEmpty()){
            mapa.put("error","Ziadne loan requesty");
            return new ResponseEntity<>(mapa, HttpStatus.NO_CONTENT);
        }
        List<LoanRequest> myLoanRequests = new ArrayList<>();
        for(LoanRequest loanRequest:allLoanRequests){
            if(loanRequest.getBook().getOwner().equals(user_email) && loanRequest.getStatus().equals("PENDING")){
                myLoanRequests.add(loanRequest);
            }
        }
        if (myLoanRequests.isEmpty()){
            mapa.put("error","Ziadne loan requesty");
            return new ResponseEntity<>(mapa, HttpStatus.NO_CONTENT);
        }
        mapa.put("success",myLoanRequests);
        return new ResponseEntity<>(mapa, HttpStatus.OK);

    }

    // Accept existing request
    public ResponseEntity<Map<String,Object>> acceptLoanRequest(int request_id){

        Map<String, Object> mapa = new HashMap<>();

        Optional<LoanRequest> loanRequest = loanRequestRepository.findById(request_id);
        if (loanRequest.isEmpty()){
            mapa.put("error","LoanRequest neexistuje");
            return new ResponseEntity<>(mapa, HttpStatus.NO_CONTENT);
            //TODO po zmazani usera treba zmazat knihy a requesty
        }
        loanRequest.get().setStatus("ACCEPTED");
        loanRequest.get().getBook().setStatus("RENTED");

        loanRequestRepository.save(loanRequest.get());
        bookRepository.save(loanRequest.get().getBook());

        messageService.sendMessage(
                "Žiadosť o knihu " +
                        loanRequest.get().getBook().getTitle()+" bola prijatá. Napíšte správu majiteľovi pre bližšie informácie.",
                loanRequest.get().getBook().getOwner(),
                loanRequest.get().getUser().getEmail());

        List<LoanRequest> otherLoanRequests = loanRequestRepository.findLoanRequestsByBook_IdAndStatus(
                loanRequest.get().getBook().getid(),"PENDING");
        for (LoanRequest otherLoanRequest:otherLoanRequests){
            otherLoanRequest.setStatus("DENIED");
            loanRequestRepository.save(otherLoanRequest);
            // This didn't work, there is a bug
            /*messageService.sendMessage(
                    "Žiadosť o knihu " +
                            loanRequest.get().getBook().getTitle()+" bola zamietnutá.",
                    loanRequest.get().getBook().getOwner(),
                    otherLoanRequest.getUser().getEmail());*/
        }
        mapa.put("success","Úspešne prijaté");
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }

    // Deny existing request
    public ResponseEntity<Map<String,Object>> denyLoanRequest(int request_id){

        Map<String, Object> mapa = new HashMap<>();

        Optional<LoanRequest> loanRequest = loanRequestRepository.findById(request_id);
        if (loanRequest.isEmpty()){
            mapa.put("error","LoanRequest neexistuje");
            return new ResponseEntity<>(mapa, HttpStatus.NO_CONTENT);

        }
        loanRequest.get().setStatus("DENIED");
        loanRequestRepository.save(loanRequest.get());

        messageService.sendMessage(
                "Žiadosť o knihu " +
                        loanRequest.get().getBook().getTitle()+" bola zamietnutá.",
                loanRequest.get().getBook().getOwner(),
                loanRequest.get().getUser().getEmail());

        mapa.put("success","Úspešne zamietnuté");
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }

    // Add new LoanRequest
    public ResponseEntity<Map<String,Object>> addNewLoanRequest(int book_id, String user_email) {
        Map<String, Object> mapa = new HashMap<>();

        Optional<User> checkUser = userRepository.findById(user_email);

        if (checkUser.isEmpty()){ // ak nie, vratime error
            mapa.put("error", "Nenašiel sa zadaný email!");
            return new ResponseEntity<>(mapa, HttpStatus.BAD_REQUEST);
        }

        Optional<Book> checkBook = bookRepository.findById(book_id);

        if (checkBook.isEmpty()){ // ak nie, vratime error
            mapa.put("error", "Nenašla sa zadaná kniha!");
            return new ResponseEntity<>(mapa, HttpStatus.BAD_REQUEST);
        }
        // We don't want multiple same loan requests

        if (loanRequestRepository.existsByBook_IdAndUser_Email(book_id,user_email)){
            mapa.put("error", "LoanRequest už existuje!");
            return new ResponseEntity<>(mapa, HttpStatus.FOUND);
        }

        // Setting the state to PENDING, because the owner doesn't accept or reject immediately
        LoanRequest loanRequest = new LoanRequest("PENDING");
        loanRequest.setBook(checkBook.get());
        loanRequest.setUser(checkUser.get());
        loanRequest.setCreated_date(new Date());


        loanRequestRepository.save(loanRequest);

        mapa.put("success", "LoanRequest bol úspešne pridaný!");
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }
}
