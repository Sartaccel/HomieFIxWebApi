package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.Feedback;
import com.sart.HomieFix.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByBookingId(Long bookingId);
    List<Feedback> findByUserProfile(UserProfile userProfile);
}