package com.ai.resume.builder.services;

import com.ai.resume.builder.dto.ResumeSummaryRequest;
import com.ai.resume.builder.dto.ResumeSummaryResponse;
import com.ai.resume.builder.exceptions.BadRequestException;
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
    public void saveResumeSummary(ResumeSummaryRequest resumeSummaryRequest, UUID resumeId) {
        if (Objects.isNull(resumeSummaryRequest) || Objects.isNull(resumeId))
            throw new BadRequestException("Summary and Resume Id can't be invalid");

        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        ResumeSummary summary = ResumeSummary.builder().details(resumeSummaryRequest.getDetails()).resume(resume).build();

        summary.setResume(resume);
        resume.setResumeSummary(summary);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resumeRepository.save(resume);
    }

    @Override
    @Cacheable(value = "summaryCache", key = "#resumeId")
    public ResumeSummaryResponse getSummary(UUID resumeId) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        ResumeSummary summary = summaryRepository.findByResume(resume);
        if (Objects.isNull(summary)) return null;
        return ResumeSummaryResponse.builder().id(summary.getId()).details(summary.getDetails()).build();
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
            throw new NoSuchElementException("No Summary Found");
        }
    }

    @Override
    @CacheEvict(value = "summaryCache", allEntries = true)
    public void updateResumeSummary(ResumeSummaryRequest resumeSummaryRequest, UUID resumeId) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);

        ResumeSummary rs = resume.getResumeSummary();
        if (!Objects.isNull(resumeSummaryRequest)) {
            rs.setDetails(resumeSummaryRequest.getDetails());
            resume.setResumeSummary(rs);
            resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
            resumeRepository.save(resume);
        } else {
            throw new NoSuchElementException("No summary found for resume with id: " + resumeId);
        }
    }
}
