package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.MobileNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MobileNumberRepository extends JpaRepository<MobileNumber, Long> {

    Optional<MobileNumber> findByMobileNumber(String mobileNumber);
}
