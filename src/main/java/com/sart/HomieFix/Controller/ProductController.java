package com.sart.HomieFix.Controller;

import com.sart.HomieFix.Entity.Product;
import com.sart.HomieFix.Service.CloudinaryService;
import com.sart.HomieFix.Service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	@Autowired
	private CloudinaryService cloudinaryService;

	// Add product
	@PostMapping("/add")
	public ResponseEntity<Product> addProduct(@RequestParam String name, @RequestParam Double price,
			@RequestPart(value = "image", required = false) MultipartFile file) {
		logger.info("Adding new product: Name={}, Price={}, Image={}", name, price, file != null);
		try {
			Product product = new Product();
			product.setName(name);
			product.setPrice(price);

			if (file != null && !file.isEmpty()) {
				String imageUrl = cloudinaryService.uploadFile(file);
				product.setProductImage(imageUrl);
			}

			Product savedProduct = productService.saveProduct(product);
			return ResponseEntity.ok(savedProduct);
		} catch (IOException e) {
			logger.error("Error uploading image: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("Error adding product: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

	// Get all products
	@GetMapping("/view")
	public ResponseEntity<List<Product>> getProducts() {
		logger.info("Fetching all products");
		List<Product> products = productService.getAllProducts();
		if (products.isEmpty()) {
			logger.warn("No products found");
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(products);
	}

	// Get product by ID
	@GetMapping("/view/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		logger.info("Fetching product with ID: {}", id);
		Optional<Product> product = Optional.ofNullable(productService.getProductById(id));
		if (product.isPresent()) {
			return ResponseEntity.ok(product.get());
		} else {
			logger.warn("Product with ID {} not found", id);
			return ResponseEntity.notFound().build();
		}
	}

	// Update product
	@PutMapping("/update/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestParam String name,
			@RequestParam Double price, @RequestPart(value = "image", required = false) MultipartFile file) {
		logger.info("Updating product with ID: {}", id);
		try {
			Product product = productService.getProductById(id);
			if (product == null) {
				logger.warn("Product with ID {} not found", id);
				return ResponseEntity.notFound().build();
			}

			product.setName(name);
			product.setPrice(price);

			if (file != null && !file.isEmpty()) {
				String imageUrl = cloudinaryService.uploadFile(file);
				product.setProductImage(imageUrl);
			}

			Product updatedProduct = productService.updateProduct(product);
			return ResponseEntity.ok(updatedProduct);
		} catch (IOException e) {
			logger.error("Error uploading image: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("Error updating product: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

	// Delete product
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		logger.info("Deleting product with ID: {}", id);
		try {
			productService.deleteProduct(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			logger.error("Error deleting product with ID {}: {}", id, e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

	// Get product suggestions based on search input
	@GetMapping("/suggestions")
	public ResponseEntity<List<Product>> getProductSuggestions(@RequestParam String searchText) {
		logger.info("Fetching product suggestions for search text: {}", searchText);
		List<Product> suggestions = productService.getProductSuggestions(searchText);
		if (suggestions.isEmpty()) {
			logger.warn("No product suggestions found for search text: {}", searchText);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(suggestions);
	}
}
