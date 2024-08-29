package com.ai.resume.builder.services;

import com.ai.resume.builder.models.Resume;

import java.util.List;
import java.util.UUID;

public interface ResumeService {
    Resume getResumeByResumeId(UUID resumeId);
    List<Resume> getResumeListOfUser(long userId);
    Resume createResume(Resume resume, long userId);
    Resume updateResume(UUID resumeId, Resume resume);
    void deleteResume(UUID resumeId);
    void updateResumeStatus(UUID resumeId);
}
