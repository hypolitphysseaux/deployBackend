package com.example.backend.message;

import com.example.backend.book.Book;
import com.example.backend.user.User;
import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table
public class Message {
    // Attributes
    @Id
    private int message_id;
    private String content;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    private Date created_date;
    private String status;

    // Constructor
    public Message() {
    }

    public Message(String content,String status) {
        this.content = content;
        this.status = status;
    }

    public Message(int message_id, String content,String status) {
        this.message_id = message_id;
        this.content = content;
        this.status = status;
    }
    // Getters & Setters
    public int getMessage_id() {
        return message_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Date getCreated_date() {
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
