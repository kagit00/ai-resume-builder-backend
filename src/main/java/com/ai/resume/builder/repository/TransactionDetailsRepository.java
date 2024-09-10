package com.ai.resume.builder.repository;

import com.ai.resume.builder.models.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, UUID> {
}
