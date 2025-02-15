package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findByMobileNumber(String mobileNumber);
}
