package com.ai.resume.builder.services;

import com.ai.resume.builder.models.ResumeSummary;

import java.util.UUID;

public interface ResumeSummaryService {
    void saveResume(ResumeSummary resumeSummary, UUID resumeId);
    ResumeSummary getSummary(UUID resumeId);
}
