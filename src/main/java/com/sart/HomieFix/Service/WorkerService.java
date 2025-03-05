package com.sart.HomieFix.Service;

import com.sart.HomieFix.Entity.Worker;
import com.sart.HomieFix.Repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkerService {

	@Autowired
	private WorkerRepository workerRepository;

	@Autowired
	private CloudinaryService cloudinaryService;

	public Worker saveWorker(String name, String role, String specification, MultipartFile profilePic, String email,
			String contactNumber, String eContactNumber, Integer workExperience, String language, LocalDate dateOfBirth,
			String gender, String houseNumber, String town, String pincode, String nearbyLandmark, String district,
			String state, String aadharNumber, String drivingLicenseNumber, LocalDate joiningDate) throws IOException {

		String imageUrl = cloudinaryService.uploadFile(profilePic);

		Worker worker = new Worker(null, name, role, specification, imageUrl, email, contactNumber, eContactNumber,
				workExperience, language, dateOfBirth, gender, houseNumber, town, pincode, nearbyLandmark, district,
				state, aadharNumber, drivingLicenseNumber, joiningDate, null, 0, true);
		return workerRepository.save(worker);
	}

	public List<Worker> getAllWorkers() {
        return workerRepository.findAll().stream()
                .filter(Worker::isActive) // Filter out inactive workers
                .collect(Collectors.toList());
    }
	
	public Worker getWorkerById(Long id) {
        return workerRepository.findById(id).filter(Worker::isActive).orElse(null); // Filter inactive workers
    }
	
	public boolean contactNumberExists(String contactNumber) {
        return workerRepository.existsByContactNumber(contactNumber);
    }

	 public Worker updateWorker(Long id, String name, String role, String specification, MultipartFile profilePic,
	            String email, String contactNumber, String eContactNumber, Integer workExperience, LocalDate dateOfBirth,
	            String gender, String houseNumber, String town, String pincode, String nearbyLandmark, String district,
	            String state, String aadharNumber, String drivingLicenseNumber, LocalDate joiningDate) throws IOException {
	        Worker existingWorker = workerRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Worker not found with id: " + id));

	        if (!existingWorker.isActive()) {
	            throw new RuntimeException("Worker is inactive and cannot be updated.");
	        }

	        // Existing update logic...
	        if (name != null && !name.isEmpty())
	            existingWorker.setName(name);
	        if (role != null && !role.isEmpty())
	            existingWorker.setRole(role);
	        if (specification != null && !specification.isEmpty())
	            existingWorker.setSpecification(specification);
	        if (profilePic != null && !profilePic.isEmpty()) {
	            String imageUrl = cloudinaryService.uploadFile(profilePic);
	            existingWorker.setProfilePicUrl(imageUrl);
	        }
	        if (email != null && !email.isEmpty())
	            existingWorker.setEmail(email);
	        if (contactNumber != null && !contactNumber.isEmpty())
	            existingWorker.setContactNumber(contactNumber);
	        if (eContactNumber != null && !eContactNumber.isEmpty())
	            existingWorker.setEContactNumber(eContactNumber);
	        if (workExperience != null)
	            existingWorker.setWorkExperience(workExperience);
	        if (dateOfBirth != null)
	            existingWorker.setDateOfBirth(dateOfBirth);
	        if (gender != null && !gender.isEmpty())
	            existingWorker.setGender(gender);
	        if (houseNumber != null && !houseNumber.isEmpty())
	            existingWorker.setHouseNumber(houseNumber);
	        if (town != null && !town.isEmpty())
	            existingWorker.setTown(town);
	        if (pincode != null && !pincode.isEmpty())
	            existingWorker.setPincode(pincode);
	        if (nearbyLandmark != null && !nearbyLandmark.isEmpty())
	            existingWorker.setNearbyLandmark(nearbyLandmark);
	        if (district != null && !district.isEmpty())
	            existingWorker.setDistrict(district);
	        if (state != null && !state.isEmpty())
	            existingWorker.setState(state);
	        if (aadharNumber != null && !aadharNumber.isEmpty())
	            existingWorker.setAadharNumber(aadharNumber);
	        if (drivingLicenseNumber != null && !drivingLicenseNumber.isEmpty())
	            existingWorker.setDrivingLicenseNumber(drivingLicenseNumber);
	        if (joiningDate != null)
	            existingWorker.setJoiningDate(joiningDate);

	        return workerRepository.save(existingWorker);
	    }

	 public void deleteWorker(Long id) {
	        Worker worker = workerRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Worker not found with id: " + id));
	        worker.setActive(false);
	        workerRepository.save(worker);
	    }
}