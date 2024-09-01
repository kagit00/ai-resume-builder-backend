package com.ai.resume.builder.services;

import com.ai.resume.builder.models.ResumeSections;

import java.util.List;
import java.util.UUID;

public interface ResumeSectionsService {
    ResumeSections saveResumeSections(ResumeSections resumeSections, UUID resumeId, String sectionType);
    List<ResumeSections> getResumeSections(UUID resumeId, String sectionType);
    void updateResumeSection(ResumeSections resumeSection, UUID resumeId, UUID resumeSectionId);
    void deleteResumeSection(UUID resumeId, UUID resumeSectionId);
}
