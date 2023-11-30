package com.example.backend.book;

import com.example.backend.loan_request.LoanRequestRepository;
import com.example.backend.user.User;
import com.example.backend.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository; //Potrebujeme aj userRepository, pretoze chceme najst usera a zvysit pocet jeho knih
    private final LoanRequestRepository loanRequestRepository;

    @Autowired
    public BookService(BookRepository bookRepository, UserRepository userRepository,LoanRequestRepository loanRequestRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.loanRequestRepository = loanRequestRepository;
    }

    // Returns all books as a list in Object in a map in ResponseEntity
    // String in map is error or success
    public ResponseEntity<Map<String,Object>> getAllBooks()
    {
        Map<String, Object> mapa = new HashMap<>();

        List<Book> allBooks = bookRepository.findAll();
        if (allBooks.isEmpty()){
            mapa.put("error", "V databáze sa nenachádzajú žiadne knihy!");
            return new ResponseEntity<>(mapa, HttpStatus.NOT_FOUND);
        }
        // This can slow down the server
        for (Book book : allBooks) {
            Optional<User> user = userRepository.findById(book.getOwner());
            book.setOwner(user.get().getMeno()+" "+user.get().getPriezvisko()+" "+user.get().getEmail());
        }
        
        mapa.put("success",allBooks);
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }

    // The same as getAllBooks, but a filter is applied
    public ResponseEntity<Map<String,Object>> getBooks(String string) {

        Map<String, Object> mapa = new HashMap<>();

        List<Book> searchedBooks = bookRepository.findBookByTitleContainingOrAuthorContaining(string,string);

        if (searchedBooks.isEmpty()){
            mapa.put("error", "V databáze sa nenachádzajú žiadne knihy s týmto stringom!");
            return new ResponseEntity<>(mapa, HttpStatus.NOT_FOUND);
        }
        // Toto moze spomalovat server pri vela zaznamoch
        for (Book book : searchedBooks) {
            Optional<User> user = userRepository.findById(book.getOwner());
            book.setOwner(user.get().getMeno()+" "+user.get().getPriezvisko()+" "+user.get().getEmail());
        }

        mapa.put("success",searchedBooks);
        return new ResponseEntity<>(mapa, HttpStatus.OK);

    }


    // Add new book
    public ResponseEntity<Map<String,Object>> addNewBook(String title, String author, String owner) {
        //Mapa, ktoru funkcia vrati v body
        Map<String, Object> mapa = new HashMap<>();

        Optional<User> check = userRepository.findById(owner);

        if (check.isEmpty()){ // ak nie, vratime error
            mapa.put("error", "Nenašiel sa zadaný email!");
            return new ResponseEntity<>(mapa, HttpStatus.BAD_REQUEST);
        }
        int number_of_books=check.get().getPocet_knih();
        check.get().setPocet_knih(++number_of_books);

        Book book = new Book(title,author,owner);
        book.setCreated_date(new Date());
        bookRepository.save(book);

        mapa.put("success", "Kniha bola úspešne pridaná!");
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }

    // Delete a book available only for admin
    @Transactional
    public ResponseEntity<Map<String,Object>> deleteBook(String name_email, int id) {
        Map<String, Object> mapa = new HashMap<>();

        // Here it's better to use relation in database
        // But a lot of bugs in other parts of the code apperead when tried to change it
        String[] list_owner = name_email.split(" ");
        String email = list_owner[list_owner.length-1];


        Optional<User> checkUser = userRepository.findById(email);
        Optional<Book> checkBook = bookRepository.findById(id);

        if (checkBook.isEmpty()){ // ak nie, vratime error
            mapa.put("error", "Nenašla sa kniha!");
            return new ResponseEntity<>(mapa, HttpStatus.NOT_FOUND);
        }

        if (checkUser.isEmpty()){ // ak nie, vratime error
            mapa.put("error", "Nenašiel sa vlastník knihy!");
            return new ResponseEntity<>(mapa, HttpStatus.NOT_FOUND);
        }
        int number_of_books=checkUser.get().getPocet_knih();
        checkUser.get().setPocet_knih(--number_of_books);

        loanRequestRepository.deleteLoanRequestsByBook_Id(id);
        bookRepository.deleteById(id);

        mapa.put("success", "Kniha bola úspešne odstránená!");
        return new ResponseEntity<>(mapa, HttpStatus.OK);
    }
}
