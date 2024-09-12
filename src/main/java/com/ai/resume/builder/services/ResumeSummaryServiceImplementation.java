package com.ai.resume.builder.services;

import com.ai.resume.builder.cache.Cache;
import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.ResumeSummary;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.repository.SummaryRepository;
import com.ai.resume.builder.utilities.BasicUtility;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @CachePut(value = "summaryCache", key = "#resumeId", unless = "#result == null")
    public ResumeSummary saveResumeSummary(ResumeSummary resumeSummary, UUID resumeId) {
        if (Objects.isNull(resumeSummary) || Objects.isNull(resumeId)) {
            throw new InternalServerErrorException("summary and resume id can't be null " + resumeId);
        }
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);

        resumeSummary.setResume(resume);
        resume.setResumeSummary(resumeSummary);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());

        summaryRepository.save(resumeSummary);
        resumeRepository.save(resume);
        return resumeSummary;
    }

    @Override
    @Cacheable(value = "summaryCache", key = "#resumeId")
    public ResumeSummary getSummary(UUID resumeId) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        return summaryRepository.findByResume(resume);
    }

    @Override
    @CacheEvict(value = "summaryCache", allEntries = true)
    public void deleteSummary(UUID resumeId) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        ResumeSummary resumeSummary = resume.getResumeSummary();

        if (!Objects.isNull(resumeSummary)) {
            resume.setResumeSummary(null);
            resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
            resumeRepository.save(resume);
        } else {
            throw new NoSuchElementException("No summary found for resume with id: " + resumeId);
        }
    }

    @Override
    @CachePut(value = "summaryCache", key = "#resumeId", unless = "#result == null")
    public void updateResume(ResumeSummary resumeSummary, UUID resumeId) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);

        ResumeSummary rs = resume.getResumeSummary();
        if (!Objects.isNull(rs)) {
            rs.setDetails(resumeSummary.getDetails());
            rs.setResume(resume);
            resume.setResumeSummary(rs);
            resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
            resumeRepository.save(resume);
        } else {
            throw new NoSuchElementException("No summary found for resume with id: " + resumeId);
        }
    }
}
