package com.ai.resume.builder.controllers;

import com.ai.resume.builder.dto.ResumeSummaryRequest;
import com.ai.resume.builder.dto.ResumeSummaryResponse;
import com.ai.resume.builder.services.ResumeSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Resume Summary API", description = "Operations related to resume summary")
public class SummaryController {
    private final ResumeSummaryService resumeSummaryService;

    @Operation(summary = "Save Resume Summary",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @PostMapping(value = "/{resumeId}/summary", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveResumeSummary(@RequestBody ResumeSummaryRequest resumeSummaryRequest, @PathVariable("resumeId") String resumeId) {
        resumeSummaryService.saveResumeSummary(resumeSummaryRequest, UUID.fromString(resumeId));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Get Resume Summary",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @GetMapping(value = "/{resumeId}/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResumeSummaryResponse> getResumeSummary(@PathVariable("resumeId") String resumeId) {
        return new ResponseEntity<>(resumeSummaryService.getSummary(UUID.fromString(resumeId)), HttpStatus.OK);
    }

    @Operation(summary = "Update Resume Summary",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @PutMapping(value = "/{resumeId}/summary", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateResumeSummary(@RequestBody ResumeSummaryRequest resumeSummaryRequest, @PathVariable("resumeId") String resumeId) {
        resumeSummaryService.updateResumeSummary(resumeSummaryRequest, UUID.fromString(resumeId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete Resume Summary",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @DeleteMapping(value = "/{resumeId}/summary")
    public ResponseEntity<Object> deleteSummary(@PathVariable("resumeId") String resumeId) {
        resumeSummaryService.deleteSummary(UUID.fromString(resumeId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
