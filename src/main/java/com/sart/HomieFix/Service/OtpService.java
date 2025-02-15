package com.sart.HomieFix.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.sart.HomieFix.Configuration.OtpConfiguration;
import com.sart.HomieFix.Entity.MobileNumber;
import com.sart.HomieFix.Entity.UserProfile;
import com.sart.HomieFix.Repository.MobileNumberRepository;
import com.sart.HomieFix.Repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class OtpService {

    @Autowired
    private OtpConfiguration otpConfiguration;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private MobileNumberRepository mobileNumberRepository;

    // Map to store OTP and its expiration time
    private final Map<String, OtpDetails> otpMap = new HashMap<>();

    public String sendOtpToPhone(String mobileNumber) {
        String otp = generateOtp();
        long expirationTime = System.currentTimeMillis() + 60000; // 1 minute validity

        PhoneNumber recipientPhoneNumber = new PhoneNumber(mobileNumber);
        PhoneNumber senderPhoneNumber = new PhoneNumber(otpConfiguration.getPhoneNumber());
        String msgBody = "Your HomieFix Login OTP is: " + otp;

        // Send OTP via Twilio
        Message.creator(recipientPhoneNumber, senderPhoneNumber, msgBody).create();

        // Store OTP and expiration time in memory (do not store in DB yet)
        otpMap.put(mobileNumber, new OtpDetails(otp, expirationTime));

        return "OTP sent successfully!";
    }

    public String validateOtp(String mobileNumber, String otp) {
        if (otpMap.containsKey(mobileNumber)) {
            OtpDetails otpDetails = otpMap.get(mobileNumber);

            if (System.currentTimeMillis() > otpDetails.getExpirationTime()) {
                otpMap.remove(mobileNumber);
                return "OTP has expired. Please request a new one.";
            }

            if (otpDetails.getOtp().equals(otp)) {
                otpMap.remove(mobileNumber);

                Optional<MobileNumber> mobileOptional = mobileNumberRepository.findByMobileNumber(mobileNumber); // Correct type

                if (mobileOptional.isPresent()) { // Check if MobileNumber exists
                    MobileNumber mobile = mobileOptional.get(); // Get the MobileNumber entity
                    return "Mobile number " + mobileNumber + " verified successfully. First Login : " + mobile.getFirstLoginDate();
                } else { // MobileNumber does not exist
                    MobileNumber mobile = new MobileNumber(mobileNumber);
                    mobile.setFirstLoginDate(LocalDate.now());
                    mobileNumberRepository.save(mobile);

                    UserProfile userProfile = new UserProfile("", "", mobile);
                    userProfileRepository.save(userProfile);

                    return "Mobile number " + mobileNumber + " verified successfully (new user created)."; // Indicate new user
                }
            }
        }
        return "Invalid OTP or mobile number.";
    }

    private String generateOtp() {
        int otp = (int) (Math.random() * 10000);
        return String.format("%04d", otp);
    }

    // Inner class to hold OTP details
    private static class OtpDetails {
        private final String otp;
        private final long expirationTime;

        public OtpDetails(String otp, long expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }

        public String getOtp() {
            return otp;
        }

        public long getExpirationTime() {
            return expirationTime;
        }
    }
}