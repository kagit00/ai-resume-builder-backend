package com.ai.resume.builder.services;

import com.ai.resume.builder.models.ResumeAnalysisResult;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeAnalysisService {
    ResumeAnalysisResult analyzeResume(String resumeContent, String jobDescription);
    String extractTextFromPdfImage(MultipartFile file);
}
