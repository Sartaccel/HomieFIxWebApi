package com.sart.HomieFix.Controller;

import com.sart.HomieFix.Entity.Worker;
import com.sart.HomieFix.Service.WorkerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/workers")
public class WorkerController {

	private static final Logger logger = LoggerFactory.getLogger(WorkerController.class);

	@Autowired
	private WorkerService workerService;

	@PostMapping("/add")
	public ResponseEntity<Worker> addWorker(@RequestParam String name, @RequestParam String role,
			@RequestParam String specification, @RequestParam MultipartFile profilePic, @RequestParam String email,
			@RequestParam String contactNumber, @RequestParam String eContactNumber,
			@RequestParam Integer workExperience, @RequestParam String language,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
			@RequestParam String gender, @RequestParam String houseNumber, @RequestParam String town,
			@RequestParam String pincode, @RequestParam String nearbyLandmark, @RequestParam String district,
			@RequestParam String state, @RequestParam String aadharNumber, @RequestParam String drivingLicenseNumber,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate joiningDate) {

		logger.info("Adding new worker: {}", name); // Log the worker's name

		try {
			Worker savedWorker = workerService.saveWorker(name, role, specification, profilePic, email, contactNumber,
					eContactNumber, workExperience, language, dateOfBirth, gender, houseNumber, town, pincode,
					nearbyLandmark, district, state, aadharNumber, drivingLicenseNumber, joiningDate);
			logger.info("Worker added successfully: {}", savedWorker.getId()); // Log the ID after successful save
			return ResponseEntity.ok(savedWorker);
		} catch (IOException e) {
			logger.error("Error adding worker: {}", e.getMessage(), e); // Log the exception with details
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/view")
    public ResponseEntity<List<Worker>> getWorkers() {
        logger.info("Viewing all workers");
        List<Worker> workers = workerService.getAllWorkers();
        if (workers.isEmpty()) {
            logger.info("No active workers found");
            return ResponseEntity.noContent().build();
        } else {
            logger.info("Found {} active workers", workers.size());
            return ResponseEntity.ok(workers);
        }
    }
	
	@GetMapping("/view/{id}")
    public ResponseEntity<Worker> getWorkerById(@PathVariable Long id) {
        logger.info("Viewing worker with ID: {}", id);
        Worker worker = workerService.getWorkerById(id);
        if (worker == null) {
            logger.warn("Active worker with ID {} not found", id);
            return ResponseEntity.notFound().build();
        } else {
            logger.info("Found active worker: {}", worker.getName());
            return ResponseEntity.ok(worker);
        }
    }

	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable Long id) {
        logger.info("Deleting worker with ID: {}", id);
        try {
            workerService.deleteWorker(id);
            logger.info("Worker soft deleted successfully: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error deleting worker: {}", e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }


	@PutMapping("/update/{id}")
    public ResponseEntity<Worker> updateWorker(@PathVariable Long id, @RequestParam(required = false) String name,
            @RequestParam(required = false) String role, @RequestParam String specification,
            @RequestParam(required = false) MultipartFile profilePic, @RequestParam(required = false) String email,
            @RequestParam(required = false) String contactNumber, @RequestParam(required = false) String eContactNumber,
            @RequestParam(required = false) Integer workExperience,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam(required = false) String gender, @RequestParam(required = false) String houseNumber,
            @RequestParam(required = false) String town, @RequestParam(required = false) String pincode,
            @RequestParam(required = false) String nearbyLandmark, @RequestParam(required = false) String district,
            @RequestParam(required = false) String state, @RequestParam(required = false) String aadharNumber,
            @RequestParam(required = false) String drivingLicenseNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate joiningDate) {
        logger.info("Updating worker with ID: {}", id);
        try {
            Worker updatedWorker = workerService.updateWorker(id, name, role, specification, profilePic, email,
                    contactNumber, eContactNumber, workExperience, dateOfBirth, gender, houseNumber, town, pincode,
                    nearbyLandmark, district, state, aadharNumber, drivingLicenseNumber, joiningDate);
            logger.info("Worker updated successfully: {}", id);
            return ResponseEntity.ok(updatedWorker);
        } catch (IOException e) {
            logger.error("Error updating worker: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            logger.warn("Worker with ID {} not found for update or is inactive", id);
            return ResponseEntity.notFound().build();
        }
    }
}