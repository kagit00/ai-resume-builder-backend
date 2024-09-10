package com.ai.resume.builder.services;

import com.ai.resume.builder.models.ResumeSummary;
import java.util.UUID;

public interface ResumeSummaryService {
    ResumeSummary saveResumeSummary(ResumeSummary resumeSummary, UUID resumeId);
    ResumeSummary getSummary(UUID resumeId);
    void deleteSummary(UUID resumeId);
    void updateResume(ResumeSummary resumeSummary, UUID resumeId);
}
