package com.sart.HomieFix.Service;

import com.sart.HomieFix.Entity.Cart;
import com.sart.HomieFix.Entity.Product;
import com.sart.HomieFix.Entity.UserProfile;
import com.sart.HomieFix.Repository.CartRepository;
import com.sart.HomieFix.Repository.ProductRepository;
import com.sart.HomieFix.Repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ProductRepository productRepository;

    // Add service to cart
    public Cart addToCart(Long userProfileId, Long productId) {
        UserProfile userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if the service is already in the cart
        if (cartRepository.findByUserProfileAndProduct(userProfile, product).isPresent()) {
            throw new RuntimeException("This service is already in the cart.");
        }

        // Add new service to cart
        Cart cart = new Cart(userProfile, product);
        cart.setQuantity(1); // Always set quantity to 1
        return cartRepository.save(cart);
    }

    // View all cart items for a user
    public List<Cart> getCartItems(Long userProfileId) {
        UserProfile userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found"));
        return cartRepository.findByUserProfile(userProfile);
    }

    // Remove service from cart
    public void removeCartItem(Long id) {
        cartRepository.deleteById(id);
    }

    // Calculate total price of services in the cart
    public Double calculateTotalPrice(Long userProfileId) {
        UserProfile userProfile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found"));
        List<Cart> cartItems = cartRepository.findByUserProfile(userProfile);

        return cartItems.stream()
                .mapToDouble(cart -> cart.getProduct().getPrice()) // No quantity multiplication
                .sum();
    }
}
