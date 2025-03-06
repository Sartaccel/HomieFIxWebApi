package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.BookingNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingNotificationRepository extends JpaRepository<BookingNotification, Long> {
    List<BookingNotification> findByCreatedAtBefore(LocalDateTime date);
    
    List<BookingNotification> findByCreatedAtAfter(LocalDateTime date);
}