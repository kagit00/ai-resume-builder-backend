package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.ResumeSections;
import com.ai.resume.builder.services.ResumeSectionsServiceImplementation;
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
public class ResumeSectionsController {
    private final ResumeSectionsServiceImplementation resumeSectionsServiceImplementation;

    @PostMapping(value = "/{resumeId}/{resumeSection}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveResumeSection(@RequestBody ResumeSections resumeSections, @PathVariable("resumeId") String resumeId, @PathVariable("resumeSection") String resumeSection) {
        resumeSectionsServiceImplementation.saveResumeSections(resumeSections, UUID.fromString(resumeId), resumeSection);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/{resumeId}/{resumeSection}")
    public ResponseEntity<List<ResumeSections>> getResumeSectionsList(@PathVariable("resumeId") String resumeId, @PathVariable("resumeSection") String resumeSection) {
        return new ResponseEntity<>(this.resumeSectionsServiceImplementation.getResumeSections(UUID.fromString(resumeId)), HttpStatus.OK);
    }
}
