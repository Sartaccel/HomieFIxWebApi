package com.sart.HomieFix.Controller;

import com.sart.HomieFix.Entity.MobileNumber;
import com.sart.HomieFix.Repository.MobileNumberRepository;
import com.sart.HomieFix.Service.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/mobile")
public class MobileNumberController {

    private static final Logger logger = LoggerFactory.getLogger(MobileNumberController.class);

    @Autowired
    private OtpService otpService;

    @Autowired
    private MobileNumberRepository mobileNumberRepository;

    @PostMapping("/sendOtp")
    public ResponseEntity<String> sendOtp(@RequestParam String mobileNumber) {
        logger.info("Received request to send OTP for mobile number: {}", mobileNumber);
        try {
            String response = otpService.sendOtpToPhone(mobileNumber);
            logger.info("OTP sent successfully to mobile number: {}", mobileNumber);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error sending OTP to mobile number {}: {}", mobileNumber, e.getMessage());
            return ResponseEntity.badRequest().body("Error sending OTP: " + e.getMessage());
        }
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestParam String mobileNumber, @RequestParam String otp) {
        logger.info("Received request to verify OTP for mobile number: {}", mobileNumber);
        try {
            String response = otpService.validateOtp(mobileNumber, otp);
            logger.info("OTP verification result for {}: {}", mobileNumber, response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error verifying OTP for {}: {}", mobileNumber, e.getMessage());
            return ResponseEntity.badRequest().body("Error verifying OTP: " + e.getMessage());
        }
    }

    @GetMapping("/getFirstLoginDate")
    public ResponseEntity<String> getFirstLoginDate(@RequestParam String mobileNumber) {
        logger.info("Fetching first login date for mobile number: {}", mobileNumber);
        try {
            Optional<MobileNumber> mobile = Optional.ofNullable(mobileNumberRepository.findByMobileNumber(mobileNumber));

            if (mobile.isPresent() && mobile.get().getFirstLoginDate() != null) {
                String firstLoginDate = mobile.get().getFirstLoginDate().toString();
                logger.info("First login date for mobile number {}: {}", mobileNumber, firstLoginDate);
                return ResponseEntity.ok("First login date for mobile number " + mobileNumber + " is: " + firstLoginDate);
            }

            logger.warn("No first login date found for mobile number: {}", mobileNumber);
            return ResponseEntity.ok("No first login date found for mobile number: " + mobileNumber);
        } catch (Exception e) {
            logger.error("Error fetching first login date for {}: {}", mobileNumber, e.getMessage());
            return ResponseEntity.badRequest().body("Error fetching first login date: " + e.getMessage());
        }
    }
}
