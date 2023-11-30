package com.example.backend.book;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Get all books in a list
    @GetMapping(path = "/getAllBooks")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String,Object>> getAllBooks()
    {
        return bookService.getAllBooks();
    }

    // Get books that you are searching for
    // Post mapping, because a string comes from front-end for the search
    @PostMapping(path="/getBooks" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String,Object>> getBooks(
            @RequestParam("string") String string
    )
    {
        return bookService.getBooks(string);
    }
    // Adding new book
    @CrossOrigin(origins = "*")
    @PostMapping(path = "/addNewBook", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String,Object>> addNewBook(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("owner") String userEmail
    ){
        return bookService.addNewBook(title, author, userEmail);
    }

    // Delete book
    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/deleteBook", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String,Object>> deleteBook(
            @RequestParam("email") String email,
            @RequestParam("id") int id)
    {
        return bookService.deleteBook(email, id);
    }
}

