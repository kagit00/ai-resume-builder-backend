package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.AdditionalDetails;
import com.ai.resume.builder.models.Language;
import com.ai.resume.builder.services.AdditionalDetailsServiceImplementation;
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
public class AdditionalDetailsController {
    private final AdditionalDetailsServiceImplementation additionalDetailsServiceImplementation;

    @PostMapping(value = "/{resumeId}/additional-details", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveAdditionalDetails(@RequestBody AdditionalDetails additionalDetails, @PathVariable("resumeId") String resumeId) {
        this.additionalDetailsServiceImplementation.saveAdditionalDetails(additionalDetails, UUID.fromString(resumeId));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/{resumeId}/additional-details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdditionalDetails> getAdditionalDetails(@PathVariable("resumeId") String resumeId) {
        return new ResponseEntity<>(this.additionalDetailsServiceImplementation.getAdditionalDetails(UUID.fromString(resumeId)), HttpStatus.OK);
    }
}
