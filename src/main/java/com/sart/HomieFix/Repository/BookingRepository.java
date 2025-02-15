package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.Booking;
import com.sart.HomieFix.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserProfile(UserProfile userProfile);
}
