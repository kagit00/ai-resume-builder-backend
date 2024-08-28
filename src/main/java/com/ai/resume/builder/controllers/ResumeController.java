package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.services.ResumeServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resume")
public class ResumeController {
    private final ResumeServiceImplementation resumeServiceImplementation;

    public ResumeController(ResumeServiceImplementation resumeServiceImplementation) {
        this.resumeServiceImplementation = resumeServiceImplementation;
    }

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

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Resume>> getResumeListByUserId(@PathVariable("userId") long userId) {
        List<Resume> resumes = resumeServiceImplementation.getResumeListOfUser(userId);
        return new ResponseEntity<>(resumes, HttpStatus.OK);
    }
}
