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
import java.util.List;
import java.util.Optional;
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

    public List<UserProfile> getUserProfile(String mobileNumber) {
        logger.info("Fetching profile for mobile number: {}", mobileNumber);
        Optional<MobileNumber> mobileOptional = mobileNumberRepository.findByMobileNumber(mobileNumber);

        if (mobileOptional.isEmpty()) {
            logger.warn("Mobile number not found: {}", mobileNumber);
            throw new RuntimeException("Mobile number not found.");
        }

        MobileNumber mobile = mobileOptional.get();
        List<UserProfile> userProfile = userProfileRepository.findByMobileNumberId(mobile.getId());

        if (userProfile == null ||!userProfile.stream().anyMatch(UserProfile::isActive)) {
            logger.warn("No active profile found for mobile number: {}", mobileNumber);
            throw new RuntimeException("Active profile not found.");
        }

        logger.info("Profile fetched successfully for {}", mobileNumber);
        return userProfile;
    }

    @Transactional
    public String verifyAndSendEmail(String email, String mobileNumber) {
        logger.info("Verifying email {} for mobile {}", email, mobileNumber);
        Optional<MobileNumber> mobileOptional = mobileNumberRepository.findByMobileNumber(mobileNumber);

        if (mobileOptional.isEmpty()) {
            logger.warn("Mobile number not verified: {}", mobileNumber);
            return "Mobile number not verified!";
        }

        MobileNumber mobile = mobileOptional.get();
        UserProfile profile = userProfileRepository.findByMobileNumberId(mobile.getId()).stream().findFirst().orElse(null);

        if (profile == null) {
            profile = new UserProfile("", email, mobile);
            profile.setActive(true); // Set isActive to true
            userProfileRepository.save(profile);
            logger.info("New user profile created for mobile: {}", mobileNumber);
        } else {
            profile.setEmail(email);
            userProfileRepository.save(profile);
            logger.info("Updated email {} for mobile: {}", email, mobileNumber);
        }

        VerificationToken existingToken = verificationTokenRepository.findByUserProfile(profile);
        if (existingToken!= null && existingToken.getExpirationTime().isAfter(LocalDateTime.now())) {
            logger.info("Token already sent for mobile: {}", mobileNumber);
            return "Token already sent! Please check your email.";
        } else if (existingToken!= null) {
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
        Optional<MobileNumber> mobileOptional = mobileNumberRepository.findByMobileNumber(mobileNumber);

        if (mobileOptional.isEmpty()) {
            logger.warn("Mobile number not found: {}", mobileNumber);
            return false;
        }

        MobileNumber mobile = mobileOptional.get();
        UserProfile profile = userProfileRepository.findByMobileNumberId(mobile.getId()).stream().findFirst().orElse(null);
        boolean isVerified = profile!= null && profile.isEmailVerified();
        logger.info("Email verification status for {}: {}", mobileNumber, isVerified);
        return isVerified;
    }

    @Transactional
    public String saveOrUpdateUserProfile(String fullName, String email, String mobileNumber) {
        logger.info("Saving/updating user profile for mobile: {}", mobileNumber);
        Optional<MobileNumber> mobileOptional = mobileNumberRepository.findByMobileNumber(mobileNumber);

        // Use orElseGet to optimize Optional handling
        UserProfile profile = mobileOptional.map(mobile -> userProfileRepository.findByMobileNumberId(mobile.getId()).stream().findFirst().orElse(null))
                .orElseGet(() -> {
                    // Mobile number doesn't exist, create new mobile number and profile
                    MobileNumber newMobile = new MobileNumber(mobileNumber);
                    mobileNumberRepository.save(newMobile);
                    return new UserProfile(fullName, email, newMobile);
                });

        if (profile!= null &&!profile.isActive()) {
            // Mobile number exists and is inactive, create a *new* profile
            profile = new UserProfile(fullName, email, profile.getMobileNumber()); // Associate with the existing mobile
            profile.setActive(true); // Ensure new profile is active
        } else if (profile!= null) {
            // Mobile number exists and is active, update existing profile
            profile.setFullName(fullName);
            profile.setEmail(email);
        } else {
            // Mobile number doesn't exist, create new profile with new mobile number
            MobileNumber newMobile = new MobileNumber(mobileNumber);
            mobileNumberRepository.save(newMobile);
            profile = new UserProfile(fullName, email, newMobile);
            profile.setActive(true);
        }

        userProfileRepository.save(profile);

        if (profile.getId() == null) {
            logger.info("New mobile number and user profile created for mobile: {}", mobileNumber);
            return "New mobile number and user profile created successfully.";
        } else if (!profile.isActive()) { // Check if it was a new profile created for an inactive mobile
            logger.info("New user profile created for mobile: {}", mobileNumber);
            return "New user profile created successfully.";
        } else {
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

    @Transactional
    public void deleteUser(String mobileNumber) {
        logger.info("Deleting user with mobile number: {}", mobileNumber);
        Optional<MobileNumber> mobileOptional = mobileNumberRepository.findByMobileNumber(mobileNumber);

        if (mobileOptional.isEmpty()) {
            logger.warn("Mobile number not found: {}", mobileNumber);
            throw new RuntimeException("Mobile number not found.");
        }

        MobileNumber mobile = mobileOptional.get();
        List<UserProfile> userProfiles = userProfileRepository.findByMobileNumberId(mobile.getId()); // Fetch all profiles

        if (userProfiles.isEmpty()) {
            logger.warn("No user found with mobile number: {}", mobileNumber);
            throw new RuntimeException("User not found.");
        }

        for (UserProfile userProfile: userProfiles) { // Deactivate all profiles
            userProfile.setActive(false);
            userProfileRepository.save(userProfile);
            logger.info("User profile with ID {} deactivated successfully.", userProfile.getId());
        }

        logger.info("All user profiles with mobile number {} deactivated successfully.", mobileNumber);
    }
}