package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.ResumeSection;
import com.ai.resume.builder.services.ResumeSectionsServiceImplementation;
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
public class ResumeSectionsController {
    private final ResumeSectionsServiceImplementation resumeSectionsServiceImplementation;

    @Transactional
    @PostMapping(value = "/{resumeId}/{sectionType}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResumeSection> saveResumeSection(@RequestBody ResumeSection resumeSection, @PathVariable("resumeId") String resumeId, @PathVariable("sectionType") String sectionType) {
        return new ResponseEntity<>(
                resumeSectionsServiceImplementation.saveResumeSection(resumeSection, UUID.fromString(resumeId), sectionType),
                HttpStatus.CREATED
        );
    }

    @Transactional
    @GetMapping(value = "/{resumeId}/{sectionType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResumeSection>> getResumeSectionsList(@PathVariable("resumeId") String resumeId, @PathVariable("sectionType") String sectionType) {
        return new ResponseEntity<>(this.resumeSectionsServiceImplementation.getResumeSections(UUID.fromString(resumeId), sectionType), HttpStatus.OK);
    }

    @Transactional
    @PutMapping(value = "/{resumeId}/{resumeSectionId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateResumeSection(@RequestBody ResumeSection resumeSection, @PathVariable("resumeId") String resumeId, @PathVariable("resumeSectionId") String resumeSectionId) {
        resumeSectionsServiceImplementation.updateResumeSection(resumeSection, UUID.fromString(resumeId), UUID.fromString(resumeSectionId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping(value = "/{resumeId}/{resumeSectionId}")
    public ResponseEntity<Object> deleteResumeSection(@PathVariable("resumeId") String resumeId, @PathVariable("resumeSectionId") String resumeSectionId) {
        resumeSectionsServiceImplementation.deleteResumeSection(UUID.fromString(resumeId), UUID.fromString(resumeSectionId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
