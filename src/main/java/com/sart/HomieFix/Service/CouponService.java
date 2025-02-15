package com.sart.HomieFix.Service;

import com.sart.HomieFix.Entity.Coupon;
import com.sart.HomieFix.Entity.MobileNumber; // Import MobileNumber
import com.sart.HomieFix.Entity.UserCoupon;
import com.sart.HomieFix.Entity.UserProfile;
import com.sart.HomieFix.Repository.CouponRepository;
import com.sart.HomieFix.Repository.MobileNumberRepository; // Import MobileNumberRepository
import com.sart.HomieFix.Repository.UserCouponRepository;
import com.sart.HomieFix.Repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private MobileNumberRepository mobileNumberRepository; // Inject MobileNumberRepository

    // Create a global coupon (No expiration date handling)
    public Coupon createGlobalCoupon(String code, Double discountPercentage) {
        Coupon coupon = new Coupon(code, discountPercentage);
        return couponRepository.save(coupon);
    }

    // Check if a user can use a coupon (using MobileNumber)
    public boolean canUserUseCoupon(String mobileNumber, String couponCode) {
        MobileNumber mobile = mobileNumberRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("Mobile number not found"));

        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        Optional<UserCoupon> userCoupon = userCouponRepository.findByMobileNumberAndCoupon(mobile, coupon);

        return userCoupon.isEmpty() || !userCoupon.get().isUsed();
    }

    // Mark a coupon as used for a user (using MobileNumber)
    public void markCouponAsUsed(String mobileNumber, String couponCode) {
        MobileNumber mobile = mobileNumberRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("Mobile number not found"));

        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        Optional<UserCoupon> userCouponOptional = userCouponRepository.findByMobileNumberAndCoupon(mobile, coupon);

        UserCoupon userCoupon = userCouponOptional.orElseGet(() -> new UserCoupon(mobile, coupon)); // No need to set isUsed here, it's false by default
        userCoupon.setUsed(true); // Now you can set it.

        userCouponRepository.save(userCoupon);
    }

    // Get all available coupons
    public List<Coupon> getCoupons() { // Renamed for clarity
        return couponRepository.findAll();
    }


    //Find coupon by code
    public Coupon findCouponByCode(String code){
        return couponRepository.findByCode(code).orElseThrow(()-> new RuntimeException("Coupon not found"));
    }
}