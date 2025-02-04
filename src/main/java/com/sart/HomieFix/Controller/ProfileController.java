package com.sart.HomieFix.Controller;

import com.sart.HomieFix.Service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private ProfileService profileService;

    // Get user profile
    @PostMapping("/get")
    public ResponseEntity<?> getProfile(@RequestBody Map<String, String> requestBody) {
        String mobileNumber = requestBody.get("mobileNumber");
        logger.info("Received request to fetch profile for mobile number: {}", mobileNumber);

        try {
            Object profile = profileService.getUserProfile(mobileNumber);
            if (profile != null) {
                return ResponseEntity.ok(profile);
            } else {
                logger.warn("Profile not found for mobile number: {}", mobileNumber);
                return ResponseEntity.status(404).body("Profile not found for mobile number: " + mobileNumber);
            }
        } catch (Exception e) {
            logger.error("Error fetching profile for {}: {}", mobileNumber, e.getMessage());
            return ResponseEntity.badRequest().body("Error fetching profile: " + e.getMessage());
        }
    }

    // Verify and send verification email
    @PostMapping("/verifyAndSendEmail")
    public ResponseEntity<?> verifyAndSendEmail(@RequestParam String email, @RequestParam String mobileNumber) {
        logger.info("Received request to verify email: {} for mobile: {}", email, mobileNumber);

        try {
            String result = profileService.verifyAndSendEmail(email, mobileNumber);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error verifying and sending email: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error verifying email: " + e.getMessage());
        }
    }

    // Check if email is verified
    @GetMapping("/isEmailVerified")
    public ResponseEntity<Boolean> isEmailVerified(@RequestParam String mobileNumber) {
        logger.info("Checking if email is verified for mobile number: {}", mobileNumber);

        try {
            boolean isVerified = profileService.isEmailVerified(mobileNumber);
            return ResponseEntity.ok(isVerified);
        } catch (Exception e) {
            logger.error("Error checking email verification for {}: {}", mobileNumber, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Save or update profile
    @PostMapping("/saveOrUpdate")
    public ResponseEntity<String> saveOrUpdateProfile(@RequestParam String fullName, @RequestParam String email, @RequestParam String mobileNumber) {
        logger.info("Saving or updating profile for mobile: {}", mobileNumber);

        try {
            String result = profileService.saveOrUpdateUserProfile(fullName, email, mobileNumber);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error saving/updating profile for {}: {}", mobileNumber, e.getMessage());
            return ResponseEntity.badRequest().body("Error updating profile: " + e.getMessage());
        }
    }

    // Verify email with token
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        logger.info("Verifying email with token: {}", token);

        try {
            String result = profileService.verifyEmail(token);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error verifying email token {}: {}", token, e.getMessage());
            return ResponseEntity.badRequest().body("Email verification failed: " + e.getMessage());
        }
    }
}
