package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.Language;
import com.ai.resume.builder.services.LanguageServiceImplementation;
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
    private final LanguageServiceImplementation languageServiceImplementation;

    @Transactional
    @PostMapping(value = "/{resumeId}/language", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Language> saveLanguage(@PathVariable("resumeId") String resumeId, @RequestBody @Valid Language language) {
        return new ResponseEntity<>(this.languageServiceImplementation.saveLanguage(UUID.fromString(resumeId), language), HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping(value = "/{resumeId}/language", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Language>> getLanguages(@PathVariable("resumeId") String resumeId) {
        return new ResponseEntity<>(this.languageServiceImplementation.getLanguages(UUID.fromString(resumeId)), HttpStatus.OK);
    }

    @Transactional
    @PutMapping(value = "/{resumeId}/language/{languageId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateLanguage(@PathVariable("resumeId") String resumeId, @PathVariable("languageId") String languageId, @RequestBody @Valid Language language) {
        this.languageServiceImplementation.updateLanguage(UUID.fromString(resumeId), UUID.fromString(languageId), language);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping(value = "/{resumeId}/language/{languageId}")
    public ResponseEntity<Object> deleteLanguage(@PathVariable("resumeId") String resumeId, @PathVariable("languageId") String languageId) {
        this.languageServiceImplementation.deleteLanguage(UUID.fromString(resumeId), UUID.fromString(languageId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
