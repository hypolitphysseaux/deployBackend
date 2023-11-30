package com.example.backend.loan_request;

import com.example.backend.book.Book;
import com.example.backend.user.User;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class LoanRequest {
    // Attributes
    @Id
    private int request_id;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String status;
    private Date created_date;

    // Constructors
    public LoanRequest() {
    }

    public LoanRequest(String status) {
        this.status = status;
    }

    public LoanRequest(int request_id, String status) {
        this.request_id = request_id;
        this.status = status;
    }
    // Getters & Setters
    public int getRequest_id() {
        return request_id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Date getCreatedDate() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }
}
