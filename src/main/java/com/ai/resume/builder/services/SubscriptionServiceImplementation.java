package com.ai.resume.builder.services;

import com.ai.resume.builder.cache.Cache;
import com.ai.resume.builder.models.BraintreeClientToken;
import com.ai.resume.builder.models.CheckoutRequest;
import com.ai.resume.builder.models.User;
import com.ai.resume.builder.models.UserRole;
import com.ai.resume.builder.repository.RoleRepository;
import com.ai.resume.builder.repository.UserRepository;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@AllArgsConstructor
@Service
public class SubscriptionServiceImplementation implements SubscriptionService {
    private final BraintreeGateway braintreeGateway;
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionServiceImplementation.class);
    private final Cache cache;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public Result<Transaction> subscribe(CheckoutRequest request, long userId) {
        TransactionRequest transactionRequest = new TransactionRequest()
                .amount(request.getAmount())
                .paymentMethodNonce(request.getPaymentMethodNonce())
                .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> transactionResult = braintreeGateway.transaction().sale(transactionRequest);
        logger.debug("Transaction Result: {}: ", transactionResult.getTransaction());

        if (transactionResult.isSuccess()) {
            User user = cache.getUserById(userId);
            if (user.isFreeUser() && !user.isPremiumUser()) {
                Set<UserRole> userRoles = DefaultValuesPopulator.populateDefaultPremiumUserRoles(user, roleRepository);
                for (UserRole ur : userRoles)
                    roleRepository.save(ur.getRole());
                user.getRoles().addAll(userRoles);
                logger.debug("user has these roles: {}", user.getRoles());
                userRepository.save(user);
            }
        }
        return transactionResult;
    }

    @Override
    public BraintreeClientToken generateClientToken() {
        return new BraintreeClientToken(braintreeGateway.clientToken().generate());
    }
}
