package com.ai.resume.builder.repository;

import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.ResumeSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SummaryRepository extends JpaRepository<ResumeSummary, UUID> {
    ResumeSummary findByResume(Resume resume);
}
