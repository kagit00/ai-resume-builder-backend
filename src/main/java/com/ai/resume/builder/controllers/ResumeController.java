package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.SkillsDTO;
import com.ai.resume.builder.services.ResumeService;
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
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/resume")
@AllArgsConstructor
@Tag(name = "Resume API", description = "Operations related resume")
public class ResumeController {
    private final ResumeService resumeService;

    @Operation(summary = "Get Resume By ResumeId",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @GetMapping(value = "/{resumeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resume> getResumeByResumeId(@PathVariable("resumeId") String resumeId) {
        Resume resume = resumeService.getResumeByResumeId(UUID.fromString(resumeId));
        return new ResponseEntity<>(resume, HttpStatus.OK);
    }

    @Operation(summary = "Create Resume",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @PostMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resume> createResume(@RequestBody Resume resume, @PathVariable("userId") long userId) {
        Resume createdResume = resumeService.createResume(resume, userId);
        return new ResponseEntity<>(createdResume, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete Resume",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @DeleteMapping(value = "/{resumeId}")
    public ResponseEntity<Void> deleteResume(@PathVariable("resumeId") String resumeId) {
        resumeService.deleteResume(UUID.fromString(resumeId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Get ResumeList By UserId",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<Resume>>> getResumeListByUserId(@PathVariable("userId") long userId) {
        Map<String, List<Resume>> resumes = resumeService.getResumeListOfUser(userId);
        return new ResponseEntity<>(resumes, HttpStatus.OK);
    }

    @Operation(summary = "Update Resume Status",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @PutMapping(value = "/{resumeId}/status-update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateStatus(@PathVariable("resumeId") String resumeId) {
        resumeService.updateResumeStatus(UUID.fromString(resumeId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Get Skills",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @GetMapping(value = "/{resumeId}/skills", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getSkills(@PathVariable("resumeId") String resumeId) {
        return new ResponseEntity<>(resumeService.getSkills(UUID.fromString(resumeId)), HttpStatus.OK);
    }

    @Operation(summary = "Update Skills",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @PutMapping(value = "/{resumeId}/skills", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> updateSkills(@PathVariable("resumeId") String resumeId, @RequestBody @Valid SkillsDTO skills) {
        resumeService.updateSkills(UUID.fromString(resumeId), skills);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
