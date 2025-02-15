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

    @PostMapping("/create")
    public ResponseEntity<Coupon> createCoupon(@RequestParam String code, @RequestParam Double discountPercentage) {
        logger.info("Creating coupon with code: {} and discount: {}", code, discountPercentage);
        try {
            Coupon coupon = couponService.createGlobalCoupon(code, discountPercentage);
            logger.info("Coupon created successfully: {}", coupon);
            return ResponseEntity.ok(coupon);
        } catch (Exception e) {
            logger.error("Error creating coupon: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateCoupon(@RequestParam String mobileNumber, @RequestParam String couponCode) {
        logger.info("Validating coupon: {} for mobile number: {}", couponCode, mobileNumber);
        try {
            boolean canUse = couponService.canUserUseCoupon(mobileNumber, couponCode);
            if (canUse) {
                logger.info("Coupon {} is valid for mobile number {}", couponCode, mobileNumber);
                return ResponseEntity.ok("Coupon is valid for this mobile number");
            } else {
                logger.warn("Coupon {} is invalid or already used by mobile number {}", couponCode, mobileNumber);
                return ResponseEntity.badRequest().body("Coupon is invalid or already used by this mobile number");
            }
        } catch (Exception e) {
            logger.error("Error validating coupon: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/use")
    public ResponseEntity<String> useCoupon(@RequestParam String mobileNumber, @RequestParam String couponCode) {
        logger.info("Marking coupon {} as used for mobile number {}", couponCode, mobileNumber);
        try {
            couponService.markCouponAsUsed(mobileNumber, couponCode);
            logger.info("Coupon {} successfully marked as used for mobile number {}", couponCode, mobileNumber);
            return ResponseEntity.ok("Coupon marked as used for this mobile number");
        } catch (Exception e) {
            logger.error("Error using coupon: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/view")
    public ResponseEntity<List<Coupon>> getCoupons() {
        logger.info("Fetching available coupons");
        try {
            List<Coupon> coupons = couponService.getCoupons();
            logger.info("Coupons fetched: {}", coupons.size());
            return ResponseEntity.ok(coupons);
        } catch (Exception e) {
            logger.error("Error fetching coupons: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/find")
    public ResponseEntity<Coupon> findCouponByCode(@RequestParam String code) {
        try {
            Coupon coupon = couponService.findCouponByCode(code);
            return ResponseEntity.ok(coupon);
        } catch (Exception e) {
            logger.error("Error fetching coupon: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
