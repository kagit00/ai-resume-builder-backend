package com.ai.resume.builder.repository;

import com.ai.resume.builder.models.Language;
import com.ai.resume.builder.models.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LanguageRepository extends JpaRepository<Language, UUID> {
    List<Language> findByResume(Resume resume);
}
