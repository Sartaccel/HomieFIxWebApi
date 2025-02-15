package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.Cart;
import com.sart.HomieFix.Entity.Product;
import com.sart.HomieFix.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserProfileAndProduct(UserProfile userProfile, Product product);

    List<Cart> findByUserProfile(UserProfile userProfile);
}