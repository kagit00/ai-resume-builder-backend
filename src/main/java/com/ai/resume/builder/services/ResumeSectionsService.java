package com.ai.resume.builder.services;

import com.ai.resume.builder.dto.ResumeSectionRequest;
import com.ai.resume.builder.dto.ResumeSectionResponse;
import java.util.List;
import java.util.UUID;

public interface ResumeSectionsService {
    ResumeSectionResponse saveResumeSection(ResumeSectionRequest resumeSectionRequest, UUID resumeId, String sectionType);
    List<ResumeSectionResponse> getResumeSections(UUID resumeId, String sectionType);
    void updateResumeSection(ResumeSectionRequest resumeSectionRequest, UUID resumeId, UUID resumeSectionId);
    void deleteResumeSection(UUID resumeId, UUID resumeSectionId);
}
