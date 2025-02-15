package com.sart.HomieFix.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class MobileNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mobileNumber;

    private LocalDate firstLoginDate; // New column

    public MobileNumber(Long id, String mobileNumber, LocalDate firstLoginDate) {
        super();
        this.id = id;
        this.mobileNumber = mobileNumber;
        this.firstLoginDate = firstLoginDate;
    }

    // Constructors, Getters, and Setters
    public MobileNumber() {
    }

    public MobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public LocalDate getFirstLoginDate() {
        return firstLoginDate;
    }

    public void setFirstLoginDate(LocalDate firstLoginDate) {
        this.firstLoginDate = firstLoginDate;
    }
}