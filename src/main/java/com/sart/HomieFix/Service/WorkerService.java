package com.sart.HomieFix.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sart.HomieFix.Entity.Worker;
import com.sart.HomieFix.Repository.WorkerRepository;

import java.io.IOException;
import java.util.List;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private CloudinaryService cloudinaryService; // Inject CloudinaryService

    // Save worker with profile picture (uploaded to Cloudinary)
    public Worker saveWorker(String name, String role, MultipartFile profilePic) throws IOException {
        String imageUrl = cloudinaryService.uploadFile(profilePic); // Upload to Cloudinary

        Worker worker = new Worker();
        worker.setName(name);
        worker.setRole(role);
        worker.setProfilePicUrl(imageUrl); // Store Cloudinary URL in DB

        return workerRepository.save(worker);
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Worker getWorkerById(Long id) {
        return workerRepository.findById(id).orElse(null);
    }

 // Update worker details by ID
    public Worker updateWorker(Long id, String name, String role, MultipartFile profilePic) throws IOException {
        Worker existingWorker = workerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Worker not found with id: " + id));

        // Update name if provided
        if (name != null && !name.isEmpty()) {
            existingWorker.setName(name);
        }

        // Update role if provided
        if (role != null && !role.isEmpty()) {
            existingWorker.setRole(role);
        }

        // Update profile picture if provided
        if (profilePic != null && !profilePic.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(profilePic); // Upload new image to Cloudinary
            existingWorker.setProfilePicUrl(imageUrl); // Update the profile picture URL
        }

        return workerRepository.save(existingWorker);
    }

    public void deleteWorker(Long id) {
        workerRepository.deleteById(id);
    }
}
