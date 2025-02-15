package com.sart.HomieFix.Entity;

import jakarta.persistence.*;

@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;

    @Column(nullable = false)
    private boolean emailVerified = false;

    @ManyToOne  // Changed to ManyToOne
    @JoinColumn(name = "mobile_id") // Removed unique=true
    private MobileNumber mobileNumber;

    @Column(nullable = false)
    private boolean isActive = true; // New field for user status


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public MobileNumber getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(MobileNumber mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public UserProfile() {
        this.emailVerified = false;
        this.isActive = true; // Initialize isActive in the default constructor
    }

    public UserProfile(String fullName, String email, MobileNumber mobileNumber) {
        this.fullName = fullName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.emailVerified = false;
        this.isActive = true; // Initialize isActive in the parameterized constructor
    }

}