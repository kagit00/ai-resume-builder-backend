package com.ai.resume.builder.services;

import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.SkillsDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ResumeService {
    Resume getResumeByResumeId(UUID resumeId);
    Map<String, List<Resume>> getResumeListOfUser(long userId);
    Resume createResume(Resume resume, long userId);
    Resume updateResume(UUID resumeId, Resume resume);
    void deleteResume(UUID resumeId);
    void updateResumeStatus(UUID resumeId);
    void updateSkills(UUID resumeId, SkillsDTO skills);
    List<String> getSkills(UUID resumeId);
}
