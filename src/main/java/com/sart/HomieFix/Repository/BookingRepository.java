package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.Booking;
import com.sart.HomieFix.Entity.UserProfile;
import com.sart.HomieFix.Entity.Worker;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByUserProfile(UserProfile userProfile);

	Optional<Booking> findById(Long bookingId);
	
	List<Booking> findByWorker(Worker worker);
}
