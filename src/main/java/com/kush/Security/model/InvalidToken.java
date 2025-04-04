package com.kush.Security.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "invalid_token_table") // creating the table to store the invalid tokens
public class InvalidToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Column(nullable = false)
    private Date expirationDate;  // Keep expirationDate as Date if you want to stick with it

    // Constructor with token and expirationDate (Date type)
    public InvalidToken(String token, Date expirationDate) {
        this.token = token;
        this.expirationDate = expirationDate;
    }

    // Constructor with ID, token, and expirationDate (Date type)
    public InvalidToken(Long id, String token, Date expirationDate) {
        this.id = id;
        this.token = token;
        this.expirationDate = expirationDate;
    }

    // Default constructor
    public InvalidToken() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
