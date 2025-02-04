package com.sart.HomieFix.Controller;

import com.sart.HomieFix.Entity.Coupon;
import com.sart.HomieFix.Service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    private CouponService couponService;

    // Create a global coupon
    @PostMapping("/create")
    public ResponseEntity<Coupon> createCoupon(
            @RequestParam String code,
            @RequestParam Double discountPercentage) {
        logger.info("Creating coupon with code: {} and discount: {}", code, discountPercentage);
        Coupon coupon = couponService.createGlobalCoupon(code, discountPercentage);
        logger.info("Coupon created successfully: {}", coupon);
        return ResponseEntity.ok(coupon);
    }

    // Check if a user can use a coupon
    @GetMapping("/validate")
    public ResponseEntity<String> validateCoupon(@RequestParam Long userId, @RequestParam String couponCode) {
        logger.info("Validating coupon: {} for user: {}", couponCode, userId);
        boolean canUse = couponService.canUserUseCoupon(userId, couponCode);
        if (canUse) {
            logger.info("Coupon {} is valid for user {}", couponCode, userId);
            return ResponseEntity.ok("Coupon is valid for this user");
        } else {
            logger.warn("Coupon {} is invalid or already used by user {}", couponCode, userId);
            return ResponseEntity.badRequest().body("Coupon is invalid or already used by this user");
        }
    }

    // Mark a coupon as used for a user
    @PostMapping("/use")
    public ResponseEntity<String> useCoupon(@RequestParam Long userId, @RequestParam String couponCode) {
        logger.info("Marking coupon {} as used for user {}", couponCode, userId);
        couponService.markCouponAsUsed(userId, couponCode);
        logger.info("Coupon {} successfully marked as used for user {}", couponCode, userId);
        return ResponseEntity.ok("Coupon marked as used for this user");
    }

    // Get all coupons available to a user
    @GetMapping("/view")
    public ResponseEntity<List<Coupon>> getCouponsForUser() {
        logger.info("Fetching available coupons");
        List<Coupon> coupons = couponService.getCouponsForUser();
        logger.info("Coupons fetched: {}", coupons.size());
        return ResponseEntity.ok(coupons);
    }
}
