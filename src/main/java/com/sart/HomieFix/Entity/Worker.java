package com.sart.HomieFix.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Worker {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String role;
	private String specification;
	

	private String profilePicUrl;
	private String email;
	private String contactNumber;
	private String eContactNumber;
	private Integer workExperience;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	private String gender;
	private String houseNumber;
	private String town;
	private String pincode;
	private String nearbyLandmark;
	private String district;
	private String state;
	private String aadharNumber;
	private String drivingLicenseNumber;

	@Column(name = "joining_date")
	private LocalDate joiningDate;

	@Column(name = "average_rating")
	private Double averageRating;

	// Getters and Setters

	public Worker() {

	}

	public Long getId() {
		return id;
	}

	public Worker(Long id, String name, String role,String specification, String profilePicUrl, String email, String contactNumber,
			String eContactNumber, Integer workExperience, LocalDate dateOfBirth, String gender, String houseNumber,
			String town, String pincode, String nearbyLandmark, String district, String state, String aadharNumber,
			String drivingLicenseNumber, LocalDate joiningDate, Double averageRating) {

		this.id = id;
		this.name = name;
		this.role = role;
		this.specification=specification;
		this.profilePicUrl = profilePicUrl;
		this.email = email;
		this.contactNumber = contactNumber;
		this.eContactNumber = eContactNumber;
		this.workExperience = workExperience;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.houseNumber = houseNumber;
		this.town = town;
		this.pincode = pincode;
		this.nearbyLandmark = nearbyLandmark;
		this.district = district;
		this.state = state;
		this.aadharNumber = aadharNumber;
		this.drivingLicenseNumber = drivingLicenseNumber;
		this.joiningDate = joiningDate;
		this.averageRating = averageRating;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getProfilePicUrl() {
		return profilePicUrl;
	}

	public void setProfilePicUrl(String profilePicUrl) {
		this.profilePicUrl = profilePicUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEContactNumber() {
		return eContactNumber;
	}

	public void setEContactNumber(String eContactNumber) {
		this.eContactNumber = eContactNumber;
	}

	public Integer getWorkExperience() {
		return workExperience;
	}

	public void setWorkExperience(Integer workExperience) {
		this.workExperience = workExperience;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getNearbyLandmark() {
		return nearbyLandmark;
	}

	public void setNearbyLandmark(String nearbyLandmark) {
		this.nearbyLandmark = nearbyLandmark;
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

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getDrivingLicenseNumber() {
		return drivingLicenseNumber;
	}

	public void setDrivingLicenseNumber(String drivingLicenseNumber) {
		this.drivingLicenseNumber = drivingLicenseNumber;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

	public Double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Double averageRating) {
        if (averageRating != null) {
            // Round to one decimal place
            this.averageRating = Math.round(averageRating * 10) / 10.0;
        } else {
            this.averageRating = null;
        }
    }
}