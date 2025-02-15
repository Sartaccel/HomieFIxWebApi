package com.sart.HomieFix.Service;

import com.sart.HomieFix.Entity.Feedback;
import com.sart.HomieFix.Entity.UserProfile;
import com.sart.HomieFix.Repository.FeedbackRepository;
import com.sart.HomieFix.Repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    // Submit feedback for a booking by user
    public Feedback submitFeedback(Long userId, Long bookingId, int rating, String comment) {
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Feedback feedback = new Feedback();
        feedback.setUserProfile(userProfile);
        feedback.setBookingId(bookingId);
        feedback.setRating(rating);
        feedback.setComment(comment);

        return feedbackRepository.save(feedback);
    }

    // Get feedbacks by bookingId
    public List<Feedback> getFeedbackByBookingId(Long bookingId) {
        return feedbackRepository.findByBookingId(bookingId);
    }

    // Get feedbacks by user profile
    public List<Feedback> getFeedbackByUserProfile(Long userId) {
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return feedbackRepository.findByUserProfile(userProfile);
    }

    // Get feedback by ID
    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
    }

    // Update feedback by ID
    public Feedback updateFeedback(Long id, int rating, String comment) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        feedback.setRating(rating);
        feedback.setComment(comment);

        return feedbackRepository.save(feedback);
    }
}