package com.ai.resume.builder.controllers;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.ResumeAnalysisResult;
import com.ai.resume.builder.services.ResumeAnalysisService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/resume/analysis")
@AllArgsConstructor
public class ResumeAnalysisController {
    private final ResumeAnalysisService resumeAnalysisService;

    @PostMapping(value = "/upload-and-analyze", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadAndAnalyzeResume(@RequestParam("file") MultipartFile file, @RequestParam("jobDescription") String jobDescription) {
        try {
            String resumeContent = resumeAnalysisService.extractTextFromPdfImage(file);
            ResumeAnalysisResult result = resumeAnalysisService.analyzeResume(resumeContent, jobDescription);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}

