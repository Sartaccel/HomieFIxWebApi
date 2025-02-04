package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.MobileNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MobileNumberRepository extends JpaRepository<MobileNumber, Long> {
    MobileNumber findByMobileNumber(String mobileNumber);
}
