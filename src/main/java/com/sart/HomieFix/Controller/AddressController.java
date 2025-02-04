package com.sart.HomieFix.Controller;

import com.sart.HomieFix.Entity.AddressEntity;
import com.sart.HomieFix.Service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/address")
public class AddressController {

    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private AddressService addressService;

    // Add a new address
    @PostMapping("/add")
    public ResponseEntity<?> addAddress(@RequestBody AddressEntity addressEntity) {
        logger.info("Adding new address: {}", addressEntity);
        try {
            AddressEntity savedAddress = addressService.addAddress(addressEntity);
            logger.info("Address added successfully with ID {}", savedAddress.getId());
            return ResponseEntity.ok(savedAddress);
        } catch (Exception e) {
            logger.error("Error adding address: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to add address");
        }
    }

    // Get all addresses by mobile number
    @GetMapping("/{mobileNumber}")
    public ResponseEntity<?> findByMobileNumber(@PathVariable String mobileNumber) {
        logger.info("Fetching addresses for mobile number: {}", mobileNumber);
        try {
            List<AddressEntity> addresses = addressService.findByMobileNumber(mobileNumber);
            if (addresses.isEmpty()) {
                logger.warn("No addresses found for mobile number: {}", mobileNumber);
                return ResponseEntity.noContent().build();
            }
            logger.info("Found {} addresses for mobile number: {}", addresses.size(), mobileNumber);
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            logger.error("Error fetching addresses: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to fetch addresses");
        }
    }

    // Get all addresses
    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        logger.info("Fetching all addresses");
        try {
            List<AddressEntity> addresses = addressService.findAll();
            logger.info("Retrieved {} addresses", addresses.size());
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            logger.error("Error fetching all addresses: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to fetch addresses");
        }
    }

    // Update an address by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateById(@PathVariable long id, @RequestBody AddressEntity updatedAddress) {
        logger.info("Updating address with ID: {}", id);
        try {
            Optional<AddressEntity> updatedEntity = addressService.updateById(id, updatedAddress);
            if (updatedEntity.isPresent()) {
                logger.info("Address updated successfully for ID: {}", id);
                return ResponseEntity.ok(updatedEntity.get());
            } else {
                logger.warn("Address not found for ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error updating address with ID {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to update address");
        }
    }

    // Delete an address by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        logger.info("Deleting address with ID: {}", id);
        try {
            if (addressService.deleteById(id)) {
                logger.info("Address with ID {} deleted successfully", id);
                return ResponseEntity.ok("Address with ID " + id + " has been deleted.");
            } else {
                logger.warn("Address with ID {} not found", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error deleting address with ID {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to delete address");
        }
    }
}
