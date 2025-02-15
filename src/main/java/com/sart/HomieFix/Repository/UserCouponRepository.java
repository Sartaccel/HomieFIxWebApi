package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.Coupon;
import com.sart.HomieFix.Entity.MobileNumber;
import com.sart.HomieFix.Entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    Optional<UserCoupon> findByMobileNumberAndCoupon(MobileNumber mobileNumber, Coupon coupon);

    boolean existsByMobileNumberAndCoupon(MobileNumber mobileNumber, Coupon coupon);
}