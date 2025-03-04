package com.sart.HomieFix.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class BookingNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingId;
    private String notificationType; // e.g., "NEW_BOOKING", "CANCELLED_BOOKING"
    private LocalDateTime createdAt;

    // Constructors, Getters, and Setters
    public BookingNotification() {
    }

    public BookingNotification(Long bookingId, String notificationType, LocalDateTime createdAt) {
        this.bookingId = bookingId;
        this.notificationType = notificationType;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}