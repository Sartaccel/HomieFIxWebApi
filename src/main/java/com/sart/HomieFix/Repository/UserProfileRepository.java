package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    List<UserProfile> findByMobileNumberId(Long mobileId);
}