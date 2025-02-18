package com.sart.HomieFix.Entity;

import jakarta.persistence.*;

@Entity
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String typeofAddress;
    private String mobileNumber;
    private String pincode;
    private String houseNumber;
    private String town;
    private String landmark;
    private String district;
    private String state;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    public AddressEntity() {} // Important: Add a no-args constructor

    public AddressEntity(String typeofAddress, String mobileNumber, String pincode, String houseNumber,
                         String town, String landmark, String district, String state) {
        this.typeofAddress = typeofAddress;
        this.mobileNumber = mobileNumber;
        this.pincode = pincode;
        this.houseNumber = houseNumber;
        this.town = town;
        this.landmark = landmark;
        this.district = district;
        this.state = state;
    }


    // Getters and setters for all fields (including userProfile)

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTypeofAddress() {
        return typeofAddress;
    }

    public void setTypeofAddress(String typeofAddress) {
        this.typeofAddress = typeofAddress;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}