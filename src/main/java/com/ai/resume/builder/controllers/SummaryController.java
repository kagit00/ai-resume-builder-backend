package com.ai.resume.builder.controllers;

import com.ai.resume.builder.dto.ResumeSummaryRequest;
import com.ai.resume.builder.dto.ResumeSummaryResponse;
import com.ai.resume.builder.services.ResumeSummaryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/resume")
@AllArgsConstructor
public class SummaryController {
    private final ResumeSummaryService resumeSummaryService;

    @Transactional
    @PostMapping(value = "/{resumeId}/summary", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveResumeSummary(@RequestBody ResumeSummaryRequest resumeSummaryRequest, @PathVariable("resumeId") String resumeId) {
        resumeSummaryService.saveResumeSummary(resumeSummaryRequest, UUID.fromString(resumeId));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping(value = "/{resumeId}/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResumeSummaryResponse> getResumeSummary(@PathVariable("resumeId") String resumeId) {
        return new ResponseEntity<>(resumeSummaryService.getSummary(UUID.fromString(resumeId)), HttpStatus.OK);
    }

    @Transactional
    @PutMapping(value = "/{resumeId}/summary", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateResumeSummary(@RequestBody ResumeSummaryRequest resumeSummaryRequest, @PathVariable("resumeId") String resumeId) {
        resumeSummaryService.updateResume(resumeSummaryRequest, UUID.fromString(resumeId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping(value = "/{resumeId}/summary")
    public ResponseEntity<Object> deleteSummary(@PathVariable("resumeId") String resumeId) {
        resumeSummaryService.deleteSummary(UUID.fromString(resumeId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
