package com.ai.resume.builder.repository;

import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.ResumeSections;
import com.ai.resume.builder.models.SectionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ResumeSectionsRepository extends JpaRepository<ResumeSections, UUID> {
    List<ResumeSections> findByResume(Resume resume);
    List<ResumeSections> findByResumeAndSectionType(Resume resume, SectionType sectionType);
}
