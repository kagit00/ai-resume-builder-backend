package com.ai.resume.builder.repository;

import com.ai.resume.builder.models.AdditionalDetails;
import com.ai.resume.builder.models.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdditionalDetailsRepository extends JpaRepository<AdditionalDetails, UUID> {
    AdditionalDetails findByResume(Resume resume);
}
