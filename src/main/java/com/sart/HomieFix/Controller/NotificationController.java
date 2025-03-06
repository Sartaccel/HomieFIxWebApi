package com.sart.HomieFix.Controller;

import com.sart.HomieFix.Entity.BookingNotification;
import com.sart.HomieFix.Service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private BookingService bookingService;

    @GetMapping("/recent")
    public ResponseEntity<List<BookingNotification>> getRecentNotifications() {
        logger.info("Fetching recent notifications");

        try {
            List<BookingNotification> notifications = bookingService.getRecentNotifications();
            logger.info("Successfully retrieved {} recent notifications", notifications.size());
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            logger.error("Error fetching recent notifications: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}