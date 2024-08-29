package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.Language;
import com.ai.resume.builder.models.Skill;
import com.ai.resume.builder.services.SkillsServiceImplementation;
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
public class SkillsController {
    private final SkillsServiceImplementation skillsServiceImplementation;

    @PostMapping(value = "/{resumeId}/skills", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveSkills(@RequestBody List<Skill> skills, @PathVariable("resumeId") String resumeId) {
        this.skillsServiceImplementation.saveSkills(skills, UUID.fromString(resumeId));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/{resumeId}/skills", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Skill>> getSkills(@PathVariable("resumeId") String resumeId) {
        return new ResponseEntity<>(this.skillsServiceImplementation.getSkills(UUID.fromString(resumeId)), HttpStatus.OK);
    }
}
