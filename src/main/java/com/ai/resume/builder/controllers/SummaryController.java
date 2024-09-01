package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.ResumeSummary;
import com.ai.resume.builder.services.ResumeSummaryServiceImplementation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/resume")
@AllArgsConstructor
public class SummaryController {
    private final ResumeSummaryServiceImplementation resumeSummaryServiceImplementation;

    @PostMapping(value = "/{resumeId}/summary", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveResumeSummary(@RequestBody ResumeSummary resumeSummary, @PathVariable("resumeId") String resumeId) {
        resumeSummaryServiceImplementation.saveResume(resumeSummary, UUID.fromString(resumeId));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/{resumeId}/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResumeSummary> getResumeSummary(@PathVariable("resumeId") String resumeId) {
        return new ResponseEntity<>(resumeSummaryServiceImplementation.getSummary(UUID.fromString(resumeId)), HttpStatus.OK);
    }

    @PutMapping(value = "/{resumeId}/summary", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateResumeSummary(@RequestBody ResumeSummary resumeSummary, @PathVariable("resumeId") String resumeId) {
        resumeSummaryServiceImplementation.updateResume(resumeSummary, UUID.fromString(resumeId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{resumeId}/summary")
    public ResponseEntity<?> deleteSummary(@PathVariable("resumeId") String resumeId) {
        resumeSummaryServiceImplementation.deleteSummary(UUID.fromString(resumeId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
