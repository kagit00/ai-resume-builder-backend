package com.ai.resume.builder.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PaymentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonBackReference
    private User user;

    @Column(name = "braintree_customer_id")
    private String braintreeCustomerId;

    @Column(name = "payment_method_token")
    private String paymentMethodToken;

    @Column(name = "subscription_id")
    private String subscriptionId;

    @Column(name = "subscription_status")
    private String subscriptionStatus;
}
