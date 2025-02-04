package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByMobileNumberId(Long mobileId);
}