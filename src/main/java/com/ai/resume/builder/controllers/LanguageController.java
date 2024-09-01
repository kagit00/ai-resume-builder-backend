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
    public ResponseEntity<Language> saveLanguage(@PathVariable("resumeId") String resumeId, @RequestBody Language language) {
        return new ResponseEntity<>(this.languageServiceImplementation.saveLanguage(UUID.fromString(resumeId), language), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{resumeId}/language", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Language>> getLanguages(@PathVariable("resumeId") String resumeId) {
        return new ResponseEntity<>(this.languageServiceImplementation.getLanguages(UUID.fromString(resumeId)), HttpStatus.OK);
    }

    @PutMapping(value = "/{resumeId}/language/{languageId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateLanguage(@PathVariable("resumeId") String resumeId, @PathVariable("languageId") String languageId, @RequestBody Language language) {
        this.languageServiceImplementation.updateLanguage(UUID.fromString(resumeId), UUID.fromString(languageId), language);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{resumeId}/language/{languageId}")
    public ResponseEntity<?> deleteLanguage(@PathVariable("resumeId") String resumeId, @PathVariable("languageId") String languageId) {
        this.languageServiceImplementation.deleteLanguage(UUID.fromString(resumeId), UUID.fromString(languageId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
