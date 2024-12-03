package com.ai.resume.builder.controllers;

import com.ai.resume.builder.dto.LanguageRequest;
import com.ai.resume.builder.dto.LanguageResponse;
import com.ai.resume.builder.services.LanguageService;
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
@Tag(name = "Resume Language API", description = "Operations related to languages")
public class LanguageController {
    private final LanguageService languageService;

    @Operation(summary = "Save Language",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @PostMapping(
            value = "/{resumeId}/language",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<LanguageResponse> saveLanguage(@PathVariable("resumeId") String resumeId, @RequestBody @Valid LanguageRequest languageRequest) {
        return new ResponseEntity<>(languageService.saveLanguage(UUID.fromString(resumeId), languageRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Fetch Languages Associated to Resume Id",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @GetMapping(value = "/{resumeId}/language", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LanguageResponse>> getLanguages(@PathVariable("resumeId") String resumeId) {
        return new ResponseEntity<>(languageService.getLanguages(UUID.fromString(resumeId)), HttpStatus.OK);
    }

    @Operation(summary = "Update Language Associated to Resume Id",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @PutMapping(
            value = "/{resumeId}/language/{languageId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> updateLanguage(@PathVariable("resumeId") String resumeId, @PathVariable("languageId") String languageId, @RequestBody @Valid LanguageRequest languageRequest) {
        languageService.updateLanguage(UUID.fromString(resumeId), UUID.fromString(languageId), languageRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete Language Associated to Resume Id",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @DeleteMapping(value = "/{resumeId}/language/{languageId}")
    public ResponseEntity<Object> deleteLanguage(@PathVariable("resumeId") String resumeId, @PathVariable("languageId") String languageId) {
        languageService.deleteLanguage(UUID.fromString(resumeId), UUID.fromString(languageId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
