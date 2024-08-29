package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.AISuggestion;
import com.ai.resume.builder.services.AISuggestionsServiceImplementation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resume/ai")
@AllArgsConstructor
public class AISuggestionsController {

    private final AISuggestionsServiceImplementation aiSuggestionsServiceImplementation;

    @PostMapping("/suggestions")
    public ResponseEntity<AISuggestion> generateSummary(@RequestParam String title, @RequestParam String sectionType) {
        AISuggestion aiSuggestion = aiSuggestionsServiceImplementation.generateSuggestions(title, sectionType);
        return new ResponseEntity<>(aiSuggestion, HttpStatus.OK);
    }
}
