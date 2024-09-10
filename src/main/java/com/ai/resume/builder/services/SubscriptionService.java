package com.ai.resume.builder.services;

import com.ai.resume.builder.models.BraintreeClientToken;
import com.ai.resume.builder.models.CheckoutRequest;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;

public interface SubscriptionService {
    Result<Transaction> subscribe(CheckoutRequest checkoutRequest, long userId);
    BraintreeClientToken generateClientToken();
}
