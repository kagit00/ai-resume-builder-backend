package com.ai.resume.builder.services;

import com.ai.resume.builder.cache.Cache;
import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.*;
import com.ai.resume.builder.models.TransactionDetails;
import com.ai.resume.builder.repository.*;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import com.braintreegateway.*;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@Service
public class SubscriptionServiceImplementation implements SubscriptionService {
    private final BraintreeGateway braintreeGateway;
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionServiceImplementation.class);
    private final Cache cache;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TransactionDetailsRepository transactionDetailsRepository;
    private final EmailServiceImplementation emailService;
    private final EntityManager entityManager;

    @Override
    public Result<Transaction> subscribe(CheckoutRequest request, long userId) {
        TransactionRequest transactionRequest = new TransactionRequest().amount(request.getAmount())
                .paymentMethodNonce(request.getPaymentMethodNonce()).options().submitForSettlement(true)
                .done();

        Result<Transaction> transactionResult = braintreeGateway.transaction().sale(transactionRequest);
        logger.debug("Transaction Result: {}: ", transactionResult.getTransaction());

        if (transactionResult.isSuccess()) {
            Transaction transaction = transactionResult.getTarget();
            if (Objects.isNull(transaction)) {
                logger.error("Transaction was null despite success flag");
                throw new InternalServerErrorException("Transaction was null");
            }

            User user = cache.getUserById(userId);
            if (user.isFreeUser() && !user.isPremiumUser()) {
                saveTransactionDetails(user, transaction, request);

                Role userRole = DefaultValuesPopulator.populateDefaultPremiumUserRoles(roleRepository, entityManager);

                user.getRoles().add(userRole);
                logger.debug("user has these roles: {}", user.getRoles());
                userRepository.save(user);

                if (user.isNotificationEnabled()) {
                    emailService.sendPremiumSubscriptionEmail(user.getUsername(), user.getName());
                }
            } else {
                logger.error("Transaction failed: {}", transactionResult.getMessage());
                throw new InternalServerErrorException("Transaction failed: " + transactionResult.getMessage());
            }
        }

        return transactionResult;
    }


    @Override
    public BraintreeClientToken generateClientToken() {
        return new BraintreeClientToken(braintreeGateway.clientToken().generate());
    }

    private void saveTransactionDetails(User user, Transaction transaction, CheckoutRequest request) {
        TransactionDetails transactionDetails = new TransactionDetails();
        transactionDetails.setUser(user);
        transactionDetails.setTransactionId(transaction.getId());
        transactionDetails.setAmount(request.getAmount());
        transactionDetails.setStatus(String.valueOf(transaction.getStatus()));
        transactionDetails.setCreatedAt(LocalDateTime.now());
        transactionDetailsRepository.save(transactionDetails);
    }
}
