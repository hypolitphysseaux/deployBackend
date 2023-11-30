package com.example.backend.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class User {
    // Attributes
    private String meno;
    private String priezvisko;
    @Id
    private String email;
    @JsonIgnore
    private String password_hash;
    private int pocet_knih;
    private boolean is_admin;
    private Date created_date;

    // Constructor
    public User() {
    }

    public User(String meno, String priezvisko, String email, String password_hash, boolean is_admin) {
        this.meno = meno;
        this.priezvisko = priezvisko;
        this.email = email;
        this.password_hash = password_hash;
        this.pocet_knih = 0;
        this.is_admin = is_admin;
    }

    // Getters & Setters
    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public boolean is_admin() {
        return is_admin;
    }

    public void setAdmin(boolean admin) {
        is_admin = admin;
    }

    public String getPriezvisko() {
        return priezvisko;
    }

    public void setPriezvisko(String priezvisko) {
        this.priezvisko = priezvisko;
    }

    public int getPocet_knih() {
        return pocet_knih;
    }

    public void setPocet_knih(int pocet_knih) {
        this.pocet_knih = pocet_knih;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }
}
