package com.ai.resume.builder.services;

import com.ai.resume.builder.models.ResumeAnalysisResult;

public interface ResumeAnalysisService {
    ResumeAnalysisResult analyzeResume(String resumeContent, String jobDescription);
}
