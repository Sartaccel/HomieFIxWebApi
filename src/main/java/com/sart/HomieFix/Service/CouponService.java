package com.sart.HomieFix.Service;

import com.sart.HomieFix.Entity.Coupon;
import com.sart.HomieFix.Entity.UserCoupon;
import com.sart.HomieFix.Entity.UserProfile;
import com.sart.HomieFix.Repository.CouponRepository;
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

    // Create a global coupon (No expiration date handling)
    public Coupon createGlobalCoupon(String code, Double discountPercentage) {
        Coupon coupon = new Coupon(code, discountPercentage);
        return couponRepository.save(coupon);
    }

    // Check if a user can use a coupon
    public boolean canUserUseCoupon(Long userId, String couponCode) {
        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        Optional<UserCoupon> userCoupon = userCouponRepository.findByUserAndCoupon(user, coupon);

        return userCoupon.isEmpty() || !userCoupon.get().isUsed();
    }

    // Mark a coupon as used for a user
    public void markCouponAsUsed(Long userId, String couponCode) {
        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        Optional<UserCoupon> userCouponOptional = userCouponRepository.findByUserAndCoupon(user, coupon);

        UserCoupon userCoupon = userCouponOptional.orElseGet(() -> new UserCoupon(user, coupon, true));
        userCoupon.setUsed(true);

        userCouponRepository.save(userCoupon);
    }

    // Get all available coupons
    public List<Coupon> getCouponsForUser() {
        return couponRepository.findAll();
    }
}