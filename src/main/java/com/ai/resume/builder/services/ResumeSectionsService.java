package com.ai.resume.builder.services;

import com.ai.resume.builder.models.ResumeSections;

import java.util.List;
import java.util.UUID;

public interface ResumeSectionsService {
    void saveResumeSections(ResumeSections resumeSections, UUID resumeId, String sectionType);
    List<ResumeSections> getResumeSections(UUID resumeId);
    ResumeSections updateResumeSections(ResumeSections resumeSections, UUID resumeSectionId);
}
