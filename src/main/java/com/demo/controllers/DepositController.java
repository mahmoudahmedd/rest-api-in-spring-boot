package com.demo.controllers;

import com.demo.models.User;
import com.demo.repository.UserRepository;
import com.demo.utilities.constants.AcceptableCoin;
import com.demo.utilities.exceptions.InvalidCoinException;
import com.demo.utilities.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class DepositController {

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/deposit/{userId}/{coin}")
    public ResponseEntity<?> deposit(@PathVariable(value = "coin") Integer coin,
                                     @PathVariable(value = "userId") Long userId)
            throws ResourceNotFoundException, InvalidCoinException {

        Boolean theCoinIsAcceptable = AcceptableCoin.COINS.contains(coin);

        if(theCoinIsAcceptable) {
            User user = userRepository
                    .findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));

            user.setDeposit(user.getDeposit() + coin);

            final User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(updatedUser);
        } else {
            throw new InvalidCoinException("not accepted");
        }
    }
}
