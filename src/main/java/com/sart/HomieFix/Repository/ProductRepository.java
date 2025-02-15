package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}