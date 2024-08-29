package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.ResumeSections;
import com.ai.resume.builder.models.SectionType;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.repository.ResumeSectionsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ResumeSectionsServiceImplementation implements ResumeSectionsService {
    private final ResumeRepository resumeRepository;
    private final ResumeSectionsRepository resumeSectionsRepository;

    @Override
    public void saveResumeSections(ResumeSections resumeSections, UUID resumeId, String sectionType) {
        if (Objects.isNull(resumeSections) || Objects.isNull(resumeId)) {
            throw new InternalServerErrorException("Resume id or resume section is null");
        }

        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found with id: " + resumeId)
        );

        resumeSections.setSectionType(SectionType.valueOf(sectionType));
        resumeSections.setResume(resume);
        resumeSectionsRepository.save(resumeSections);
    }

    @Override
    public List<ResumeSections> getResumeSections(UUID resumeId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found with id: " + resumeId)
        );

        return resumeSectionsRepository.findByResume(resume);
    }

    @Override
    public ResumeSections updateResumeSections(ResumeSections resumeSections, UUID resumeSectionId) {
        return null;
    }
}
