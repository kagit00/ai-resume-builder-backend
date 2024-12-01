package com.ai.resume.builder.controllers;

import com.ai.resume.builder.dto.LanguageRequest;
import com.ai.resume.builder.dto.LanguageResponse;
import com.ai.resume.builder.services.LanguageService;
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
public class LanguageController {
    private final LanguageService languageService;

    @Transactional
    @PostMapping(
            value = "/{resumeId}/language",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<LanguageResponse> saveLanguage(@PathVariable("resumeId") String resumeId, @RequestBody @Valid LanguageRequest languageRequest) {
        return new ResponseEntity<>(languageService.saveLanguage(UUID.fromString(resumeId), languageRequest), HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping(value = "/{resumeId}/language", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LanguageResponse>> getLanguages(@PathVariable("resumeId") String resumeId) {
        return new ResponseEntity<>(languageService.getLanguages(UUID.fromString(resumeId)), HttpStatus.OK);
    }

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

    @Transactional
    @DeleteMapping(value = "/{resumeId}/language/{languageId}")
    public ResponseEntity<Object> deleteLanguage(@PathVariable("resumeId") String resumeId, @PathVariable("languageId") String languageId) {
        languageService.deleteLanguage(UUID.fromString(resumeId), UUID.fromString(languageId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
