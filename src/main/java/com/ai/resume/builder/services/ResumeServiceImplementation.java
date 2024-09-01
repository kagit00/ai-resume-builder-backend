package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.ResumeStatus;
import com.ai.resume.builder.models.SkillsDTO;
import com.ai.resume.builder.models.User;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.repository.UserRepository;
import com.ai.resume.builder.utilities.Constant;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ResumeServiceImplementation implements ResumeService {
    public final ResumeRepository resumeRepository;
    public final UserRepository userRepository;

    @Override
    public Resume getResumeByResumeId(UUID resumeId) {
        return resumeRepository.findById(resumeId)
                .orElseThrow(() -> new NoSuchElementException("Resume not found with id: " + resumeId));
    }

    @Override
    public List<Resume> getResumeListOfUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("Resume not found with id" ));
        return resumeRepository.findByUser(user);

    }

    @Override
    public Resume createResume(Resume resume, long userId) {
        if (Objects.isNull(resume) || userId < 1) {
            throw new InternalServerErrorException("Resume and userId must not be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

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
    public void deleteResume(UUID resumeId) {
        this.resumeRepository.deleteById(resumeId);
    }

    @Override
    public void updateResumeStatus(UUID resumeId) {
        Resume resume = this.resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException(Constant.RUSUME_NOT_FOUND + resumeId)
        );

        resume.setStatus(ResumeStatus.COMPLETED);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resumeRepository.save(resume);
    }

    @Override
    public void updateSkills(UUID resumeId, SkillsDTO skills) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        resume.setSkills(skills.getSkills());
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resumeRepository.save(resume);
    }

    @Override
    public List<String> getSkills(UUID resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException(Constant.RUSUME_NOT_FOUND + resumeId));
        String skills = resume.getSkills();
        return !StringUtils.isEmpty(skills)? List.of(resume.getSkills().split(",")) : new ArrayList<>();
    }
}
