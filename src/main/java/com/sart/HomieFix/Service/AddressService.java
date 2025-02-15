package com.sart.HomieFix.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sart.HomieFix.Entity.AddressEntity;
import com.sart.HomieFix.Repository.AddressRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    // Add a new address
    public AddressEntity addAddress(AddressEntity addressEntity) {
        return addressRepository.save(addressEntity);
    }

    // Find all addresses by mobile number
    public List<AddressEntity> findByMobileNumber(String mobileNumber) {
        return addressRepository.findByMobileNumber(mobileNumber);
    }

    // Find all addresses
    public List<AddressEntity> findAll() {
        return addressRepository.findAll();
    }

    // Update an address by ID
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

    // Delete an address by ID
    public boolean deleteById(long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
