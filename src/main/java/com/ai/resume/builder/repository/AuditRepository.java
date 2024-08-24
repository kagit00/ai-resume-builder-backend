package com.ai.resume.builder.repository;

import com.ai.resume.builder.models.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit, Long> {
}

