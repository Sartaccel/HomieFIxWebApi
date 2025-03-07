package com.sart.HomieFix.Service;

import com.sart.HomieFix.Entity.Product;
import com.sart.HomieFix.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Save product
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Update product
    public Product updateProduct(Product product) {
        // Ensure the product exists before updating
        Optional<Product> existingProduct = productRepository.findById(product.getId());
        if (existingProduct.isPresent()) {
            return productRepository.save(product);
        } else {
            // Handle the case where the product doesn't exist (e.g., throw an exception)
            return null; // Or throw a custom exception
        }
    }

    // Delete product
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Get product by ID
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // Get product suggestions based on search input
    public List<Product> getProductSuggestions(String searchText) {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream()
                .filter(product -> product.getName().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }
}