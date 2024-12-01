package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.BraintreeClientToken;
import com.ai.resume.builder.models.CheckoutRequest;
import com.ai.resume.builder.services.SubscriptionService;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/user/subscription")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Transactional
    @GetMapping(value = "/client-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BraintreeClientToken> getClientToken() {
        return new ResponseEntity<>(subscriptionService.generateClientToken(), HttpStatus.OK);
    }

    @Transactional
    @PostMapping(value = "/checkout/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<Transaction>> subscribe(@PathVariable("userId") long userId, @RequestBody CheckoutRequest checkoutRequest) {
        return new ResponseEntity<>(subscriptionService.subscribe(checkoutRequest, userId), HttpStatus.OK);
    }
}
