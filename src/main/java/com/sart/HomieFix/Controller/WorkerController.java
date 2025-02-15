package com.sart.HomieFix.Controller;

import com.sart.HomieFix.Entity.Worker;
import com.sart.HomieFix.Service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/workers")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @PostMapping("/add")
    public ResponseEntity<Worker> addWorker(
            @RequestParam String name,
            @RequestParam String role,
            @RequestParam MultipartFile profilePic) {
        try {
            Worker savedWorker = workerService.saveWorker(name, role, profilePic);
            return ResponseEntity.ok(savedWorker);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/view")
    public ResponseEntity<List<Worker>> getWorkers() {
        List<Worker> workers = workerService.getAllWorkers();
        return workers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(workers);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Worker> getWorkerById(@PathVariable Long id) {
        Worker worker = workerService.getWorkerById(id);
        return worker != null ? ResponseEntity.ok(worker) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable Long id) {
        workerService.deleteWorker(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<Worker> updateWorker(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) MultipartFile profilePic) {
        try {
            Worker updatedWorker = workerService.updateWorker(id, name, role, profilePic);
            return ResponseEntity.ok(updatedWorker);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
