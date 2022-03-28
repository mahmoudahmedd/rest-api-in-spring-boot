package com.demo.controllers;


import com.demo.models.Product;
import com.demo.repository.ProductRepository;

import com.demo.repository.UserRepository;
import com.demo.utilities.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductById(@PathVariable(value = "id") Long productId)
            throws ResourceNotFoundException {
        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found on :: " + productId));
        return ResponseEntity.ok().body(product);
    }

    @PostMapping("/users/{userId}/products")
    public ResponseEntity<?> createProduct(@PathVariable(value = "userId") Long userId,
                                 @RequestBody Product productRequest) throws ResourceNotFoundException {

        Product product = userRepository.findById(userId).map(user -> {
            productRequest.setUser(user);
            return productRepository.save(productRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found user with id = " + userId));

        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable(value = "id") Long productId, @Valid @RequestBody Product productDetails)
            throws ResourceNotFoundException {

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product not found on :: " + productId));

        product.setProductName(productDetails.getProductName());
        product.setCost(productDetails.getCost());
        product.setAmountAvailable(productDetails.getAmountAvailable());

        final Product updatedProduct = productRepository.save(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @PreAuthorize("hasRole('SELLER') or hasRole('BUYER')")
    @DeleteMapping("/products/{id}")
    public Map<String, Boolean> deleteProduct(@PathVariable(value = "id") Long productId) throws Exception {
        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found on :: " + productId));

        productRepository.delete(product);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
