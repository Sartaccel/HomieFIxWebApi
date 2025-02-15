package com.sart.HomieFix.Service;

import com.sart.HomieFix.Entity.Product;
import com.sart.HomieFix.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return productRepository.save(product);
    }

    // Delete product
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Get product by ID
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null); // returns null if product is not found
    }

    // Get product suggestions based on search input
    public List<Product> getProductSuggestions(String searchText) {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream()
                .filter(product -> product.getName().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }
}