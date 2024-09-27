package com.ai.resume.builder.services;

import com.ai.resume.builder.dto.ResumeSummaryRequest;
import com.ai.resume.builder.dto.ResumeSummaryResponse;
import com.ai.resume.builder.models.ResumeSummary;
import java.util.UUID;

public interface ResumeSummaryService {
    void saveResumeSummary(ResumeSummaryRequest resumeSummaryRequest, UUID resumeId);
    ResumeSummaryResponse getSummary(UUID resumeId);
    void deleteSummary(UUID resumeId);
    void updateResume(ResumeSummaryRequest resumeSummaryRequest, UUID resumeId);
}
