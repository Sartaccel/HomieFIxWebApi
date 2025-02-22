package com.sart.HomieFix.Service;

import com.sart.HomieFix.Entity.Feedback;
import com.sart.HomieFix.Entity.UserProfile;
import com.sart.HomieFix.Entity.Worker;
import com.sart.HomieFix.Repository.FeedbackRepository;
import com.sart.HomieFix.Repository.UserProfileRepository;
import com.sart.HomieFix.Repository.WorkerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;
    
    @Autowired
    private WorkerRepository workerRepository;

    // Submit feedback for a booking by user
    public Feedback submitFeedback(Long userId, Long workerId, Long bookingId, int rating, String comment) {
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        Feedback feedback = new Feedback();
        feedback.setUserProfile(userProfile);
        feedback.setWorker(worker);
        feedback.setBookingId(bookingId);
        feedback.setRating(rating);
        feedback.setComment(comment);

        Feedback savedFeedback = feedbackRepository.save(feedback);
        updateWorkerAverageRating(workerId);
        return savedFeedback;
    }
    
    private void updateWorkerAverageRating(Long workerId) {
        List<Feedback> feedbacks = feedbackRepository.findByWorkerId(workerId);
        double averageRating = feedbacks.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);

        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));
        worker.setAverageRating(averageRating);
        workerRepository.save(worker);
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