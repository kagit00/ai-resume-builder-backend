package com.ai.resume.builder.controllers;

import com.ai.resume.builder.dto.ResumeSectionRequest;
import com.ai.resume.builder.dto.ResumeSectionResponse;
import com.ai.resume.builder.services.ResumeSectionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resume")
@AllArgsConstructor
@Tag(name = "Resume Sections API", description = "Operations related to resume sections")
public class ResumeSectionsController {
    private final ResumeSectionsService resumeSectionsService;

    @Operation(summary = "Save Resume Section",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @PostMapping(
            value = "/{resumeId}/{sectionType}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResumeSectionResponse> saveResumeSection(@RequestBody @Valid ResumeSectionRequest resumeSectionRequest, @PathVariable("resumeId") String resumeId, @PathVariable("sectionType") String sectionType) {
        ResumeSectionResponse response = resumeSectionsService.saveResumeSection(resumeSectionRequest, UUID.fromString(resumeId), sectionType);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get Resume Sections List",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @GetMapping(value = "/{resumeId}/{sectionType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResumeSectionResponse>> getResumeSectionsList(@PathVariable("resumeId") String resumeId, @PathVariable("sectionType") String sectionType) {
        List<ResumeSectionResponse> response = resumeSectionsService.getResumeSections(UUID.fromString(resumeId), sectionType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update Resume Section",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @PutMapping(
            value = "/{resumeId}/{resumeSectionId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> updateResumeSection(@RequestBody @Valid ResumeSectionRequest resumeSectionRequest, @PathVariable("resumeId") String resumeId, @PathVariable("resumeSectionId") String resumeSectionId) {
        resumeSectionsService.updateResumeSection(resumeSectionRequest, UUID.fromString(resumeId), UUID.fromString(resumeSectionId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete Resume Section",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @DeleteMapping(value = "/{resumeId}/{resumeSectionId}")
    public ResponseEntity<Object> deleteResumeSection(@PathVariable("resumeId") String resumeId, @PathVariable("resumeSectionId") String resumeSectionId) {
        resumeSectionsService.deleteResumeSection(UUID.fromString(resumeId), UUID.fromString(resumeSectionId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
