package com.sart.HomieFix.Controller;

import com.sart.HomieFix.Entity.Cart;
import com.sart.HomieFix.Service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addCartItem(@RequestParam Long userProfileId, @RequestParam Long productId) {
        logger.info("Adding product {} to cart for user {}", productId, userProfileId);
        try {
            Cart cart = cartService.addToCart(userProfileId, productId);
            logger.info("Product {} added to cart successfully for user {}", productId, userProfileId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            logger.error("Error adding product {} to cart for user {}: {}", productId, userProfileId, e.getMessage());
            return ResponseEntity.badRequest().body("Error adding to cart: " + e.getMessage());
        }
    }

    @GetMapping("/view/{userProfileId}")
    public ResponseEntity<?> getCart(@PathVariable Long userProfileId) {
        logger.info("Fetching cart items for user {}", userProfileId);
        try {
            List<Cart> cartItems = cartService.getCartItems(userProfileId);
            if (cartItems.isEmpty()) {
                logger.warn("No cart items found for user {}", userProfileId);
                return ResponseEntity.ok("No items found in the cart.");
            }
            logger.info("Fetched {} cart items for user {}", cartItems.size(), userProfileId);
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            logger.error("Error fetching cart items for user {}: {}", userProfileId, e.getMessage());
            return ResponseEntity.badRequest().body("Error fetching cart items: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long id) {
        logger.info("Removing cart item with ID {}", id);
        try {
            cartService.removeCartItem(id);
            logger.info("Cart item with ID {} removed successfully", id);
            return ResponseEntity.ok("Cart item removed successfully.");
        } catch (RuntimeException e) {
            logger.error("Error removing cart item with ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body("Error removing cart item: " + e.getMessage());
        }
    }

    @GetMapping("/total-price/{userProfileId}")
    public ResponseEntity<?> getTotalPrice(@PathVariable Long userProfileId) {
        logger.info("Calculating total price for user {}", userProfileId);
        try {
            Double totalPrice = cartService.calculateTotalPrice(userProfileId);
            logger.info("Total price for user {}: {}", userProfileId, totalPrice);
            return ResponseEntity.ok(totalPrice);
        } catch (Exception e) {
            logger.error("Error calculating total price for user {}: {}", userProfileId, e.getMessage());
            return ResponseEntity.badRequest().body("Error calculating total price: " + e.getMessage());
        }
    }
}
