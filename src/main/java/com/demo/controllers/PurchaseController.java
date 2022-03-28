package com.demo.controllers;

import com.demo.models.Product;
import com.demo.models.User;
import com.demo.repository.ProductRepository;
import com.demo.repository.UserRepository;
import com.demo.utilities.constants.AcceptableCoin;
import com.demo.utilities.exceptions.InvalidAmountException;
import com.demo.utilities.exceptions.InvalidCoinException;
import com.demo.utilities.exceptions.InvalidTotalCostException;
import com.demo.utilities.exceptions.ResourceNotFoundException;
import com.demo.utilities.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class PurchaseController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/buy/{userId}/product/{productId}/amount/{amounts}")
    public ResponseEntity<?> buy(@PathVariable(value = "userId") Long userId,
                                 @PathVariable(value = "productId") Long productId,
                                 @PathVariable(value = "amounts") Integer amounts)
            throws ResourceNotFoundException {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product not found on :: " + productId));

        Integer totalCost = product.getCost() * amounts;

        if(user.getDeposit() < totalCost) {
            new InvalidTotalCostException("InvalidTotalCostException " + totalCost);
        }

        if(product.getAmountAvailable() < amounts) {
            new InvalidAmountException("InvalidAmountException " + amounts);
        }

        product.setAmountAvailable(product.getAmountAvailable() -  amounts);
        user.setDeposit(user.getDeposit() - totalCost);

        final Product updatedProduct = productRepository.save(product);
        final User updatedUser = userRepository.save(user);

        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("total_spent", totalCost);
        responseMap.put("current_deposit", updatedUser.getDeposit());
        responseMap.put("product", updatedProduct);

        return ResponseEntity.ok(responseMap);
    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/reset/{userId}")
    public ResponseEntity<?> reset(@PathVariable(value = "userId") Long userId)
            throws ResourceNotFoundException {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));

        user.setDeposit(0);

        final User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }
}
