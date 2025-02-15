package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.UserProfile;
import com.sart.HomieFix.Entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    VerificationToken findByUserProfile(UserProfile userProfile);
}