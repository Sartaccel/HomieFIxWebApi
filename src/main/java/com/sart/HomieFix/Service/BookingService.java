package com.sart.HomieFix.Service;

import com.sart.HomieFix.Entity.*;
import com.sart.HomieFix.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private MobileNumberRepository mobileNumberRepository;


    public List<String> getAvailableDates() {
        List<String> availableDates = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM EEEE yyyy");

        for (int i = 1; i <= 5; i++) {
            availableDates.add(currentDate.plusDays(i).format(formatter));
        }
        return availableDates;
    }

    public List<String> getAvailableTimeSlots() {
        return Arrays.asList("9:00 AM - 11:00 AM", "11:00 AM - 1:00 PM", "1:00 PM - 3:00 PM", "3:00 PM - 5:00 PM");
    }

    public Booking createBooking(Long userProfileId, Long addressId, LocalDate bookedDate, String timeSlot, String couponCode) {
        UserProfile userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found"));

        AddressEntity deliveryAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        List<Cart> cartItems = cartRepository.findByUserProfile(userProfile);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Double totalPrice = cartItems.stream().mapToDouble(cart -> cart.getProduct().getPrice()).sum();

        if (couponCode != null && !couponCode.isEmpty()) {
            Optional<Coupon> optionalCoupon = couponRepository.findByCode(couponCode);
            if (optionalCoupon.isPresent()) {
                Coupon coupon = optionalCoupon.get();
                MobileNumber mobile = userProfile.getMobileNumber();

                if (mobile == null) {
                    throw new RuntimeException("Mobile number not found for user profile.");
                }

                if (userCouponRepository.existsByMobileNumberAndCoupon(mobile, coupon)) {
                    throw new RuntimeException("Coupon already used by this mobile number.");
                }

                totalPrice = totalPrice - (totalPrice * (coupon.getDiscountPercentage() / 100));

                UserCoupon userCoupon = new UserCoupon(mobile, coupon);
                userCoupon.setUsed(true);
                userCouponRepository.save(userCoupon);
            }
        }

        String productName = cartItems.stream()
                .map(cart -> cart.getProduct().getName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        Booking booking = new Booking(
                userProfile, deliveryAddress, LocalDate.now(), bookedDate, timeSlot,
                productName, totalPrice, "PENDING", null
        );

        bookingRepository.save(booking);
        cartRepository.deleteAll(cartItems);
        return booking;
    }
    
    public Booking rescheduleBooking(Long bookingId, LocalDate bookedDate, String timeSlot, String rescheduleReason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check if bookedDate or timeSlot has changed
        if (!booking.getBookedDate().equals(bookedDate) || !booking.getTimeSlot().equals(timeSlot)) {
            booking.setBookedDate(bookedDate);
            booking.setTimeSlot(timeSlot);
            booking.setBookingStatus("RESCHEDULED"); // Update status to RESCHEDULED
            booking.setRescheduleReason(rescheduleReason); // Set the reschedule reason
        } else {
            throw new RuntimeException("New date and time are the same as the existing ones.");
        }

        return bookingRepository.save(booking);
    }

    public Booking updateBookingStatus(Long bookingId, String status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (status.equalsIgnoreCase("CANCELLED")) {
            throw new RuntimeException("Use /cancel endpoint to cancel a booking with a reason.");
        }

        booking.setBookingStatus(status.toUpperCase());
        return bookingRepository.save(booking);
    }

    public Booking assignWorkerToBooking(Long bookingId, Long workerId, String notes) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        booking.setWorker(worker);
        booking.setBookingStatus("ASSIGNED");
        booking.setNotes(notes);

        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(Long bookingId, String cancelReason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setBookingStatus("CANCELLED");
        booking.setCancelReason(cancelReason);
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByUserProfile(Long userProfileId) {
        UserProfile userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found"));
        return bookingRepository.findByUserProfile(userProfile);
    }
}