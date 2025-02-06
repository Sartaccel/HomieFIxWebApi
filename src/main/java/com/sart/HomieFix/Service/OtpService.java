package com.sart.HomieFix.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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

    private final Map<String, OtpDetails> otpMap = new HashMap<>();

    public String sendOtpToPhone(String mobileNumber) {
        String otp = generateOtp();
        long expirationTime = System.currentTimeMillis() + 60000;

        PhoneNumber recipientPhoneNumber = new PhoneNumber(mobileNumber);
        PhoneNumber senderPhoneNumber = new PhoneNumber(otpConfiguration.getPhoneNumber());
        String msgBody = "Your HomieFix Login OTP is: " + otp;

        Message.creator(recipientPhoneNumber, senderPhoneNumber, msgBody).create();

        otpMap.put(mobileNumber, new OtpDetails(otp, expirationTime));

        return "OTP sent successfully!";
    }

    public String validateOtp(String mobileNumber, String otp) {
        if (!otpMap.containsKey(mobileNumber)) {
            return "Invalid OTP or mobile number.";
        }

        OtpDetails otpDetails = otpMap.get(mobileNumber);

        if (System.currentTimeMillis() > otpDetails.getExpirationTime()) {
            otpMap.remove(mobileNumber);
            return "OTP has expired. Please request a new one.";
        }

        if (!otpDetails.getOtp().equals(otp)) {
            return "Invalid OTP.";
        }

        otpMap.remove(mobileNumber);

        MobileNumber mobile = mobileNumberRepository.findByMobileNumber(mobileNumber);
        if (mobile == null) {
            mobile = new MobileNumber(mobileNumber);
            mobile.setFirstLoginDate(LocalDate.now());
            mobileNumberRepository.save(mobile);
            userProfileRepository.save(new UserProfile("", "", mobile));
        }

        return "Mobile number " + mobileNumber + " verified successfully. First Login: " + mobile.getFirstLoginDate();
    }

    private String generateOtp() {
        int otp = (int) (Math.random() * 10000);
        return String.format("%04d", otp);
    }

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
