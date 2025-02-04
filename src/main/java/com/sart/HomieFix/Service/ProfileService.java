package com.sart.HomieFix.Service;

import com.sart.HomieFix.Entity.MobileNumber;
import com.sart.HomieFix.Entity.UserProfile;
import com.sart.HomieFix.Entity.VerificationToken;
import com.sart.HomieFix.Repository.MobileNumberRepository;
import com.sart.HomieFix.Repository.UserProfileRepository;
import com.sart.HomieFix.Repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);

    @Autowired
    private MobileNumberRepository mobileNumberRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService emailService;

    private static final int TOKEN_EXPIRATION_MINUTES = 2; // Token expiration time

    public UserProfile getUserProfile(String mobileNumber) {
        logger.info("Fetching profile for mobile number: {}", mobileNumber);
        MobileNumber mobile = mobileNumberRepository.findByMobileNumber(mobileNumber);

        if (mobile == null) {
            logger.warn("Mobile number not found: {}", mobileNumber);
            throw new RuntimeException("Mobile number not found.");
        }

        UserProfile userProfile = userProfileRepository.findByMobileNumberId(mobile.getId());

        if (userProfile == null) {
            logger.warn("No profile found for mobile number: {}", mobileNumber);
            throw new RuntimeException("Profile not found.");
        }

        logger.info("Profile fetched successfully for {}", mobileNumber);
        return userProfile;
    }

    @Transactional
    public String verifyAndSendEmail(String email, String mobileNumber) {
        logger.info("Verifying email {} for mobile {}", email, mobileNumber);
        MobileNumber mobile = mobileNumberRepository.findByMobileNumber(mobileNumber);

        if (mobile == null) {
            logger.warn("Mobile number not verified: {}", mobileNumber);
            return "Mobile number not verified!";
        }

        UserProfile profile = userProfileRepository.findByMobileNumberId(mobile.getId());
        if (profile == null) {
            profile = new UserProfile("", email, mobile);
            userProfileRepository.save(profile);
            logger.info("New user profile created for mobile: {}", mobileNumber);
        } else {
            profile.setEmail(email);
            userProfileRepository.save(profile);
            logger.info("Updated email {} for mobile: {}", email, mobileNumber);
        }

        VerificationToken existingToken = verificationTokenRepository.findByUserProfile(profile);
        if (existingToken != null && existingToken.getExpirationTime().isAfter(LocalDateTime.now())) {
            logger.info("Token already sent for mobile: {}", mobileNumber);
            return "Token already sent! Please check your email.";
        } else if (existingToken != null) {
            verificationTokenRepository.delete(existingToken);
        }

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, profile, LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_MINUTES));
        verificationTokenRepository.save(verificationToken);

        logger.info("Verification token generated for email {} and sent.", email);
        emailService.sendVerificationEmail(email, token);

        return "Verification email sent! Please check your inbox.";
    }

    public boolean isEmailVerified(String mobileNumber) {
        logger.info("Checking email verification status for {}", mobileNumber);
        MobileNumber mobile = mobileNumberRepository.findByMobileNumber(mobileNumber);
        if (mobile == null) {
            logger.warn("Mobile number not found: {}", mobileNumber);
            return false;
        }
        UserProfile profile = userProfileRepository.findByMobileNumberId(mobile.getId());
        boolean isVerified = profile != null && profile.isEmailVerified();
        logger.info("Email verification status for {}: {}", mobileNumber, isVerified);
        return isVerified;
    }

    @Transactional
    public String saveOrUpdateUserProfile(String fullName, String email, String mobileNumber) {
        logger.info("Saving/updating user profile for mobile: {}", mobileNumber);
        MobileNumber mobile = mobileNumberRepository.findByMobileNumber(mobileNumber);

        if (mobile == null) {
            logger.error("Mobile number not verified: {}", mobileNumber);
            throw new RuntimeException("Mobile number not verified!");
        }

        UserProfile profile = userProfileRepository.findByMobileNumberId(mobile.getId());
        if (profile == null) {
            profile = new UserProfile(fullName, email, mobile);
            userProfileRepository.save(profile);
            logger.info("User profile created for mobile: {}", mobileNumber);
            return "User profile created successfully.";
        } else {
            profile.setFullName(fullName);
            profile.setEmail(email);
            userProfileRepository.save(profile);
            logger.info("User profile updated for mobile: {}", mobileNumber);
            return "User profile updated successfully.";
        }
    }

    public String verifyEmail(String token) {
        logger.info("Verifying email with token: {}", token);
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            logger.warn("Invalid token received: {}", token);
            return "Invalid token!";
        }

        if (verificationToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            logger.warn("Token expired: {}", token);
            return "Token has expired. Please request a new token.";
        }

        UserProfile profile = verificationToken.getUserProfile();
        profile.setEmailVerified(true);
        userProfileRepository.save(profile);

        verificationTokenRepository.delete(verificationToken);

        logger.info("Email verified successfully for {}", profile.getEmail());
        return "Email verified successfully!";
    }
}