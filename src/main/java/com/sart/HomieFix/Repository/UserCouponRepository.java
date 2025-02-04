package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.Coupon;
import com.sart.HomieFix.Entity.UserCoupon;
import com.sart.HomieFix.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    Optional<UserCoupon> findByUserAndCoupon(UserProfile user, Coupon coupon);
    List<UserCoupon> findAllByUser(UserProfile user);
}

