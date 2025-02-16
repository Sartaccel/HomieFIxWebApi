package com.sart.HomieFix.Service;

import com.sart.HomieFix.Entity.AddressEntity;
import com.sart.HomieFix.Entity.UserProfile;
import com.sart.HomieFix.Repository.AddressRepository;
import com.sart.HomieFix.Repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public AddressEntity addAddress(AddressEntity addressEntity, Long userId) {
        Optional<UserProfile> userProfile = userProfileRepository.findById(userId);
        if (userProfile.isEmpty()) {
            throw new RuntimeException("User profile not found");
        }
        addressEntity.setUserProfile(userProfile.get());
        return addressRepository.save(addressEntity);
    }

    public List<AddressEntity> findByMobileNumber(String mobileNumber) {
        return addressRepository.findByMobileNumber(mobileNumber);
    }

    public List<AddressEntity> findByUserId(Long userId) {
        return addressRepository.findByUserProfileId(userId);
    }

    public List<AddressEntity> findAll() {
        return addressRepository.findAll();
    }

    public Optional<AddressEntity> updateById(long id, AddressEntity updatedAddress) {
        return addressRepository.findById(id).map(addressEntity -> {
            addressEntity.setTypeofAddress(updatedAddress.getTypeofAddress());
            addressEntity.setMobileNumber(updatedAddress.getMobileNumber());
            addressEntity.setPincode(updatedAddress.getPincode());
            addressEntity.setHouseNumber(updatedAddress.getHouseNumber());
            addressEntity.setTown(updatedAddress.getTown());
            addressEntity.setLandmark(updatedAddress.getLandmark());
            addressEntity.setDistrict(updatedAddress.getDistrict());
            addressEntity.setState(updatedAddress.getState());
            return addressRepository.save(addressEntity);
        });
    }

    public boolean deleteById(long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }
}