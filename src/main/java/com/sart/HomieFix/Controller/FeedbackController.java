package com.sart.HomieFix.Controller;

import com.sart.HomieFix.Entity.Feedback;
import com.sart.HomieFix.Service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private FeedbackService feedbackService;

    // Submit feedback
    @PostMapping("/submit")
    public ResponseEntity<?> submitFeedback(
            @RequestParam Long userId,
            @RequestParam Long bookingId,
            @RequestParam int rating,
            @RequestParam String comment) {
        try {
            logger.info("Submitting feedback - UserID: {}, BookingID: {}, Rating: {}, Comment: {}", userId, bookingId, rating, comment);
            Feedback feedback = feedbackService.submitFeedback(userId, bookingId, rating, comment);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            logger.error("Error submitting feedback: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get feedback by booking ID
    @GetMapping("/byBooking/{bookingId}")
    public ResponseEntity<?> getFeedbackByBookingId(@PathVariable Long bookingId) {
        try {
            logger.info("Fetching feedback for BookingID: {}", bookingId);
            List<Feedback> feedbacks = feedbackService.getFeedbackByBookingId(bookingId);
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            logger.error("Error fetching feedback by BookingID: {} - {}", bookingId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get feedback by user profile
    @GetMapping("/byUser/{userId}")
    public ResponseEntity<?> getFeedbackByUserProfile(@PathVariable Long userId) {
        try {
            logger.info("Fetching feedback for UserID: {}", userId);
            List<Feedback> feedbacks = feedbackService.getFeedbackByUserProfile(userId);
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            logger.error("Error fetching feedback by UserID: {} - {}", userId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get feedback by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedbackById(@PathVariable Long id) {
        try {
            logger.info("Fetching feedback by ID: {}", id);
            Feedback feedback = feedbackService.getFeedbackById(id);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            logger.error("Error fetching feedback by ID: {} - {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update feedback by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFeedback(
            @PathVariable Long id,
            @RequestParam int rating,
            @RequestParam String comment) {
        try {
            logger.info("Updating feedback - ID: {}, Rating: {}, Comment: {}", id, rating, comment);
            Feedback updatedFeedback = feedbackService.updateFeedback(id, rating, comment);
            return ResponseEntity.ok(updatedFeedback);
        } catch (Exception e) {
            logger.error("Error updating feedback - ID: {} - {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
