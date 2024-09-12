package com.ai.resume.builder.services;

import com.ai.resume.builder.cache.Cache;
import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.*;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.repository.UserRepository;
import com.ai.resume.builder.utilities.BasicUtility;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ResumeServiceImplementation implements ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final Cache cache;

    @Override
    public Resume getResumeByResumeId(UUID resumeId) {
        return BasicUtility.getResumeById(resumeId, resumeRepository);
    }

    @Override
    @Cacheable(value = "resumesListCache", key = "#userId")
    public Map<String, List<Resume>> getResumeListOfUser(long userId) {
        User user = cache.getUserById(userId);

        List<Resume> allResumes = resumeRepository.findByUser(user);
        List<Resume> completedResumes = new ArrayList<>();
        List<Resume> pendingResumes = new ArrayList<>();

        for (Resume resume : allResumes) {
            if (ResumeStatus.COMPLETED.equals(resume.getStatus())) {
                completedResumes.add(resume);
            } else if (ResumeStatus.IN_PROGRESS.equals(resume.getStatus())) {
                pendingResumes.add(resume);
            }
        }
        Map<String, List<Resume>> resumes = new HashMap<>();
        resumes.put("completed_resumes", completedResumes);
        resumes.put("pending_resumes", pendingResumes);

        return resumes;
    }

    @Override
    @CachePut(value = "resumeCache", key = "#result.id",  unless = "#result == null")
    @CacheEvict(value = "resumesListCache", allEntries = true)
    public Resume createResume(Resume resume, long userId) {
        if (Objects.isNull(resume) || userId < 1) {
            throw new InternalServerErrorException("Resume and userId must not be null");
        }

        User user = cache.getUserById(userId);

        resume.setUser(user);
        resume.setCreatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resume.setStatus(ResumeStatus.IN_PROGRESS);

        return resumeRepository.save(resume);
    }

    @Override
    public Resume updateResume(UUID resumeId, Resume resume) {
        return null;
    }

    @Override
    @CacheEvict(value = {"resumeCache", "resumesListCache"}, allEntries = true)
    public void deleteResume(UUID resumeId) {
        this.resumeRepository.deleteById(resumeId);
    }

    @Override
    @CacheEvict(value = {"resumeCache", "resumesListCache"}, allEntries = true)
    public void updateResumeStatus(UUID resumeId) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        resume.setStatus(ResumeStatus.COMPLETED);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resumeRepository.save(resume);
    }

    @Override
    @CachePut(value = "skillsCache", key = "resumeId")
    public List<String> updateSkills(UUID resumeId, SkillsDTO skills) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        resume.setSkills(skills.getSkills());
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resumeRepository.save(resume);
        return List.of(resume.getSkills().split(","));
    }

    @Override
    @Cacheable(value = "skillsCache", key = "#resumeId")
    public List<String> getSkills(UUID resumeId) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        String skills = resume.getSkills();
        return !StringUtils.isEmpty(skills)? List.of(resume.getSkills().split(",")) : new ArrayList<>();
    }
}
