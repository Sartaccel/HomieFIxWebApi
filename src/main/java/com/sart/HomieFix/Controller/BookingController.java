package com.sart.HomieFix.Controller;

import com.sart.HomieFix.Entity.Booking;
import com.sart.HomieFix.Service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping("/available-dates")
    public ResponseEntity<List<String>> getAvailableDates() {
        logger.info("Fetching available dates for booking.");
        try {
            List<String> availableDates = bookingService.getAvailableDates();
            logger.info("Available dates retrieved: {}", availableDates);
            return ResponseEntity.ok(availableDates);
        } catch (Exception e) {
            logger.error("Error fetching available dates: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<String>> getAvailableTimeSlots() {
        logger.info("Fetching available time slots.");
        try {
            List<String> availableTimeSlots = bookingService.getAvailableTimeSlots();
            logger.info("Available time slots retrieved: {}", availableTimeSlots);
            return ResponseEntity.ok(availableTimeSlots);
        } catch (Exception e) {
            logger.error("Error fetching available time slots: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(
            @RequestParam Long userProfileId,
            @RequestParam Long addressId,
            @RequestParam String selectedDate,
            @RequestParam String selectedTimeSlot,
            @RequestParam(required = false) String couponCode) {

        logger.info("Creating booking for User ID: {}, Address ID: {}, Date: {}, Time Slot: {}, Coupon: {}",
                userProfileId, addressId, selectedDate, selectedTimeSlot, couponCode);

        try {
            LocalDate bookedDate = LocalDate.parse(selectedDate, dateFormatter);
            Booking booking = bookingService.createBooking(userProfileId, addressId, bookedDate, selectedTimeSlot, couponCode);
            logger.info("Booking created successfully with ID: {}", booking.getId());
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            logger.error("Error creating booking: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error creating booking: " + e.getMessage());
        }
    }

    @PutMapping("/update-status/{bookingId}")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long bookingId, @RequestParam String status) {
        logger.info("Updating booking status. Booking ID: {}, New Status: {}", bookingId, status);
        try {
            List<String> validStatuses = List.of("PENDING", "COMPLETED","STARTED"); // Include CANCELLED and ASSIGNED
            if (!validStatuses.contains(status.toUpperCase())) {
                logger.warn("Invalid status provided: {}", status);
                return ResponseEntity.badRequest().body("Invalid status. Allowed values: PENDING, COMPLETED, CANCELLED, ASSIGNED.");
            }
            Booking updatedBooking = bookingService.updateBookingStatus(bookingId, status);
            logger.info("Booking status updated successfully for Booking ID: {}", bookingId);
            return ResponseEntity.ok(updatedBooking);
        } catch (Exception e) {
            logger.error("Error updating booking status for Booking ID {}: {}", bookingId, e.getMessage());
            return ResponseEntity.badRequest().body("Error updating booking status: " + e.getMessage());
        }
    }

    @PutMapping("/assign-worker/{bookingId}")
    public ResponseEntity<?> assignWorkerToBooking(@PathVariable Long bookingId, @RequestParam Long workerId) {
        logger.info("Assigning worker to booking. Booking ID: {}, Worker ID: {}", bookingId, workerId);
        try {
            Booking updatedBooking = bookingService.assignWorkerToBooking(bookingId, workerId);
            logger.info("Worker assigned successfully to Booking ID: {}", bookingId);
            return ResponseEntity.ok(updatedBooking);
        } catch (Exception e) {
            logger.error("Error assigning worker to booking for Booking ID {}: {}", bookingId, e.getMessage());
            return ResponseEntity.badRequest().body("Error assigning worker to booking: " + e.getMessage());
        }
    }

    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId, @RequestParam String reason) {
        logger.info("Cancelling booking. Booking ID: {}, Reason: {}", bookingId, reason);
        try {
            Booking cancelledBooking = bookingService.cancelBooking(bookingId, reason);
            logger.info("Booking with ID {} cancelled successfully.", bookingId);
            return ResponseEntity.ok(cancelledBooking);
        } catch (Exception e) {
            logger.error("Error canceling booking for Booking ID {}: {}", bookingId, e.getMessage());
            return ResponseEntity.badRequest().body("Error canceling booking: " + e.getMessage());
        }
    }
    
    @PutMapping("/reschedule/{bookingId}")
    public ResponseEntity<?> rescheduleBooking(
            @PathVariable Long bookingId,
            @RequestParam String selectedDate,
            @RequestParam String selectedTimeSlot,
            @RequestParam String rescheduleReason) { // Add rescheduleReason parameter

        logger.info("Rescheduling booking. Booking ID: {}, New Date: {}, New Time Slot: {}, Reason: {}",
                bookingId, selectedDate, selectedTimeSlot, rescheduleReason);

        try {
            LocalDate bookedDate = LocalDate.parse(selectedDate, dateFormatter);
            Booking rescheduledBooking = bookingService.rescheduleBooking(bookingId, bookedDate, selectedTimeSlot, rescheduleReason);
            logger.info("Booking rescheduled successfully. Booking ID: {}", bookingId);
            return ResponseEntity.ok(rescheduledBooking);
        } catch (Exception e) {
            logger.error("Error rescheduling booking for Booking ID {}: {}", bookingId, e.getMessage());
            return ResponseEntity.badRequest().body("Error rescheduling booking: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Booking>> getAllBookings() {
        logger.info("Fetching all bookings.");
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            logger.info("Total bookings retrieved: {}", bookings.size());
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            logger.error("Error fetching all bookings: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userProfileId}")
    public ResponseEntity<?> getBookingsByUserProfile(@PathVariable Long userProfileId) {
        logger.info("Fetching bookings for User ID: {}", userProfileId);
        try {
            List<Booking> userBookings = bookingService.getBookingsByUserProfile(userProfileId);
            logger.info("User ID: {} has {} bookings", userProfileId, userBookings.size());
            return ResponseEntity.ok(userBookings);
        } catch (Exception e) {
            logger.error("Error fetching user bookings for User ID {}: {}", userProfileId, e.getMessage());
            return ResponseEntity.badRequest().body("Error fetching user bookings: " + e.getMessage());
        }
    }
}