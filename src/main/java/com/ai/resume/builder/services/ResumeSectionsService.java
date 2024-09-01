package com.ai.resume.builder.services;

import com.ai.resume.builder.models.ResumeSection;

import java.util.List;
import java.util.UUID;

public interface ResumeSectionsService {
    ResumeSection saveResumeSection(ResumeSection resumeSection, UUID resumeId, String sectionType);
    List<ResumeSection> getResumeSections(UUID resumeId, String sectionType);
    void updateResumeSection(ResumeSection resumeSection, UUID resumeId, UUID resumeSectionId);
    void deleteResumeSection(UUID resumeId, UUID resumeSectionId);
}
