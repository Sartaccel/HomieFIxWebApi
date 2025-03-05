package com.sart.HomieFix.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfile userProfile;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private AddressEntity deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private Worker worker; // Reference to the assigned worker

    private LocalDate bookingDate;
    private LocalDate bookedDate;
    private String timeSlot;
    private String productName;
    private String productImage;
   

	private Double totalPrice;
    private String bookingStatus;
    private String notes;
    private String rescheduleReason;

    private String cancelReason; // New field to store cancellation reason

    // Constructors, Getters, and Setters
    public Booking() {
    }

    public Booking(UserProfile userProfile, AddressEntity deliveryAddress, LocalDate bookingDate, LocalDate bookedDate,
                   String timeSlot, String productName, Double totalPrice,String productImage, String bookingStatus, String cancelReason) {
        this.userProfile = userProfile;
        this.deliveryAddress = deliveryAddress;
        this.bookingDate = bookingDate;
        this.bookedDate = bookedDate;
        this.timeSlot = timeSlot;
        this.productName = productName;
        this.productImage=productImage;
        this.totalPrice = totalPrice;
        this.bookingStatus = bookingStatus;
        this.cancelReason = cancelReason;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public AddressEntity getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(AddressEntity deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(LocalDate bookedDate) {
        this.bookedDate = bookedDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
    
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
    
    public String getRescheduleReason() {
        return rescheduleReason;
    }

    public void setRescheduleReason(String rescheduleReason) {
        this.rescheduleReason = rescheduleReason;
    }
}