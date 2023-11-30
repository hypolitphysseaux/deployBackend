package com.example.backend.user;

import com.example.backend.EmailService;
import com.example.backend.PasswordHasher;
import com.example.backend.book.Book;
import com.example.backend.book.BookRepository;
import com.example.backend.book.BookService;
import com.example.backend.loan_request.LoanRequestRepository;
import com.example.backend.loan_request.LoanRequestService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BookRepository bookRepository;
    private final LoanRequestRepository loanRequestRepository;

    @Autowired
    public UserService(UserRepository userRepository,EmailService emailService,
                       BookRepository bookRepository,LoanRequestRepository loanRequestRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.bookRepository = bookRepository;
        this.loanRequestRepository = loanRequestRepository;
    }

    // Returns a list of users
    public ResponseEntity<Map<String,Object>> getUsers(String string){
        Map<String, Object> mapa = new HashMap<>();

        // Searching for a user in the database that contains a certain string
        List<User> searchedUsers = userRepository.
                findUserByEmailContainingOrMenoContainingOrPriezviskoContaining
                        (string,string,string);

        if (searchedUsers.isEmpty()){
            mapa.put("error", "Nenasli sa ziadny pouzivatelia!");
            return new ResponseEntity<>(mapa, HttpStatus.NOT_FOUND);
        }


        mapa.put("success", searchedUsers);
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }

    // Register User
    public ResponseEntity<Map<String,Object>> registerNewUser(
            String email,
            String meno,
            String priezvisko,
            String password_hash,
            String check_password,
            boolean is_admin) {

        Map<String, Object> mapa = new HashMap<>();

        // Check if a user exists in a database
        Optional<User> check = userRepository.findById(email);

        if (check.isPresent()){ // We don't want to register the same user twice
            mapa.put("error", "Email je už zaregistrovaný!");
            return new ResponseEntity<>(mapa, HttpStatus.FORBIDDEN);
        }

        // Need to compare the passwords, if they are the same
        if (!(password_hash.equals(check_password))){
            mapa.put("error", "Heslá sa nezhodujú!");
            return new ResponseEntity<>(mapa, HttpStatus.NOT_ACCEPTABLE);
        }

        // Hash is created and then saved
        String encrypted = PasswordHasher.hashPassword(password_hash);

        User user = new User();
        user.setEmail(email);
        user.setMeno(meno);
        user.setPriezvisko(priezvisko);
        user.setCreated_date(new Date());
        user.setPassword_hash(encrypted);
        if (is_admin){
            user.setAdmin(true);
        }
        else {
            user.setAdmin(false);
        }

        userRepository.save(user);

        mapa.put("success", "Registrácia prebehla úspešne!");
        try {
            emailService.sendRegistrationEmail(user.getEmail(), user.getMeno());
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }


    // Login User
    public ResponseEntity<Map<String,Object>> loginUser(
            String email,
            String password_hash){

        Map<String, Object> mapa = new HashMap<>();

        Optional<User> check = userRepository.findById(email);

        if (check.isEmpty()){
            mapa.put("error", "Neregistrovaný email!");
            return new ResponseEntity<>(mapa, HttpStatus.BAD_REQUEST);
        }

        // Comparison of passwords
        String databaseHash = check.get().getPassword_hash();
        if (!(databaseHash.equals(PasswordHasher.hashPassword(password_hash)))){
            mapa.put("error", "Nesprávne heslo!");
            return new ResponseEntity<>(mapa, HttpStatus.BAD_REQUEST);
        }

        // Adding the data to the hashmap
        String mail = check.get().getEmail();
        String name = check.get().getMeno();
        boolean isAdmin = check.get().is_admin();
        mapa.put("email", mail);
        mapa.put("meno", name);
        mapa.put("is_admin", isAdmin);

        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }

    // Remove User
    @Transactional
    public ResponseEntity<Map<String,Object>> removeUser(
            String email) {

        Map<String, Object> mapa = new HashMap<>();

        Optional<User> user = userRepository.findById(email);

        if (user.isEmpty()){
            mapa.put("error", "Používateľ sa nenašiel!");
            return new ResponseEntity<>(mapa, HttpStatus.NOT_FOUND);
        }

        // Delete all requests the user added
        loanRequestRepository.deleteLoanRequestsByUser_Email(email);
        // Delete all requests he got on his books
        loanRequestRepository.deleteLoanRequestsByBook_Owner(email);
        // Delete all his books
        bookRepository.deleteBooksByOwner(email);
        // And afterwards we can delete the user
        userRepository.deleteById(email);

        mapa.put("success", "Používateľ bol úspešne vymazaný!");
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }

    // Change password
    public ResponseEntity<Map<String,Object>> changePassword(
            String email,
            String old_password,
            String new_password,
            String check_password){
        Map<String, Object> mapa = new HashMap<>();

        if(!(new_password.equals(check_password))){
            mapa.put("error", "Heslá sa nezhodujú!");
            return new ResponseEntity<>(mapa, HttpStatus.NOT_ACCEPTABLE);
        }

        Optional<User> user = userRepository.findById(email);
        if (user.isEmpty()){
            mapa.put("error", "Používateľ sa nenašiel!");
            return new ResponseEntity<>(mapa, HttpStatus.NOT_FOUND);
        }

        String databaseHash = user.get().getPassword_hash();
        if (!(databaseHash.equals(PasswordHasher.hashPassword(old_password)))){
            mapa.put("error", "Nesprávne heslo!");
            return new ResponseEntity<>(mapa, HttpStatus.BAD_REQUEST);
        }

        String encrypted = PasswordHasher.hashPassword(new_password);
        user.get().setPassword_hash(encrypted);

        userRepository.save(user.get());
        mapa.put("success", "Heslo bolo úspešne zmenené!");
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }
}
