package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.Language;
import com.ai.resume.builder.services.LanguageServiceImplementation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resume")
@AllArgsConstructor
public class LanguageController {
    private final LanguageServiceImplementation languageServiceImplementation;

    @PostMapping(value = "/{resumeId}/language", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveLanguage(@RequestBody Language language, @PathVariable("resumeId") String resumeId) {
        this.languageServiceImplementation.saveLanguage(language, UUID.fromString(resumeId));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/{resumeId}/language", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Language>> getLanguages(@PathVariable("resumeId") String resumeId) {
        return new ResponseEntity<>(this.languageServiceImplementation.getLanguages(UUID.fromString(resumeId)), HttpStatus.OK);
    }
}
