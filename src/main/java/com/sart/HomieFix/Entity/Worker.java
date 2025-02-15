package com.sart.HomieFix.Entity;

import jakarta.persistence.*;

@Entity
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String role;
    private String profilePicUrl; // Store the URL of the uploaded image

    public Worker() {}

    public Worker(Long id, String name, String role, String profilePicUrl) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.profilePicUrl = profilePicUrl;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }
}