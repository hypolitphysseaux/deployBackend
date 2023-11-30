package com.example.backend.book;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class Book {
    // Attributes
    @Id
    private int id;
    private String title;
    private String author;
    private String owner;
    private Date created_date;

    private String status;

    // Constructor
    public Book() {
    }

    public Book(int id, String title, String author, String owner) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.owner = owner;
        this.status = "AVAILABLE";
    }

    public Book(String title, String author, String owner) {
        this.title = title;
        this.author = author;
        this.owner = owner;
        this.status = "AVAILABLE";
    }

    // Getters & Setters
    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getCreatedDate() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
