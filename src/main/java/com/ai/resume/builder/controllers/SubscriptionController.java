package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.BraintreeClientToken;
import com.ai.resume.builder.models.CheckoutRequest;
import com.ai.resume.builder.services.SubscriptionService;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/user/subscription")
@Tag(name = "Subscription API", description = "Operations related to subscription")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Operation(summary = "Get Braintree Client Token",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @GetMapping(value = "/client-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BraintreeClientToken> getClientToken() {
        return new ResponseEntity<>(subscriptionService.generateClientToken(), HttpStatus.OK);
    }

    @Operation(summary = "Subscribe to a Premium User",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @PostMapping(value = "/checkout/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<Transaction>> subscribe(@PathVariable("userId") long userId, @RequestBody CheckoutRequest checkoutRequest) {
        return new ResponseEntity<>(subscriptionService.subscribe(checkoutRequest, userId), HttpStatus.OK);
    }
}
