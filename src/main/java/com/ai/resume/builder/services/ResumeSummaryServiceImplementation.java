package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.ResumeSummary;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.repository.SummaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ResumeSummaryServiceImplementation implements ResumeSummaryService {
    private final SummaryRepository summaryRepository;
    private final ResumeRepository resumeRepository;

    @Override
    public void saveResume(ResumeSummary resumeSummary, UUID resumeId) {
        if (Objects.isNull(resumeSummary) || Objects.isNull(resumeId)) {
            throw new InternalServerErrorException("summary and resume id can't be null " + resumeId);
        }

        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found with id: " + resumeId)
        );

        resumeSummary.setResume(resume);
        summaryRepository.save(resumeSummary);
    }

    @Override
    public ResumeSummary getSummary(UUID resumeId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found with id: " + resumeId)
        );

        return summaryRepository.findByResume(resume);
    }
}
