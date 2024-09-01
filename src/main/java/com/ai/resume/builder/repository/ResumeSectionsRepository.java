package com.ai.resume.builder.repository;

import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.ResumeSection;
import com.ai.resume.builder.models.SectionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ResumeSectionsRepository extends JpaRepository<ResumeSection, UUID> {
    List<ResumeSection> findByResume(Resume resume);
    List<ResumeSection> findByResumeAndSectionType(Resume resume, SectionType sectionType);
}
