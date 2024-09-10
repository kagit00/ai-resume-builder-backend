package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.SkillsDTO;
import com.ai.resume.builder.services.ResumeServiceImplementation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/resume")
@AllArgsConstructor
public class ResumeController {
    private final ResumeServiceImplementation resumeServiceImplementation;
    @GetMapping(value = "/{resumeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resume> getResumeByResumeId(@PathVariable("resumeId") String resumeId) {
        Resume resume = resumeServiceImplementation.getResumeByResumeId(UUID.fromString(resumeId));
        return new ResponseEntity<>(resume, HttpStatus.OK);
    }

    @PostMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resume> createResume(@RequestBody Resume resume, @PathVariable("userId") long userId) {
        Resume createdResume = resumeServiceImplementation.createResume(resume, userId);
        return new ResponseEntity<>(createdResume, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{resumeId}")
    public ResponseEntity<Void> deleteResume(@PathVariable("resumeId") String resumeId) {
        this.resumeServiceImplementation.deleteResume(UUID.fromString(resumeId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<Resume>>> getResumeListByUserId(@PathVariable("userId") long userId) {
        Map<String, List<Resume>> resumes = resumeServiceImplementation.getResumeListOfUser(userId);
        return new ResponseEntity<>(resumes, HttpStatus.OK);
    }

    @Transactional
    @PutMapping(value = "/{resumeId}/status-update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateStatus(@PathVariable("resumeId") String resumeId) {
        this.resumeServiceImplementation.updateResumeStatus(UUID.fromString(resumeId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @GetMapping(value = "/{resumeId}/skills", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getSkills(@PathVariable("resumeId") String resumeId) {
        return new ResponseEntity<>(this.resumeServiceImplementation.getSkills(UUID.fromString(resumeId)), HttpStatus.OK);
    }

    @Transactional
    @PutMapping(value = "/{resumeId}/skills", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> updateSkills(@PathVariable("resumeId") String resumeId, @RequestBody SkillsDTO skills) {
        this.resumeServiceImplementation.updateSkills(UUID.fromString(resumeId), skills);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
