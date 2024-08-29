package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.ResumeStatus;
import com.ai.resume.builder.models.User;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.repository.UserRepository;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

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
                () -> new NoSuchElementException("Resume not found for id: " + resumeId)
        );

        resume.setStatus(ResumeStatus.COMPLETED);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resumeRepository.save(resume);
    }
}
