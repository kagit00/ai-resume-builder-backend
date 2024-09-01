package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.ResumeSections;
import com.ai.resume.builder.models.SectionType;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.repository.ResumeSectionsRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ResumeSectionsServiceImplementation implements ResumeSectionsService {
    private final ResumeRepository resumeRepository;
    private final ResumeSectionsRepository resumeSectionsRepository;

    @Override
    public ResumeSections saveResumeSections(ResumeSections resumeSections, UUID resumeId, String sectionType) {
        if (Objects.isNull(resumeSections) || Objects.isNull(resumeId)) {
            throw new InternalServerErrorException("Resume id or resume section is null");
        }

        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found with id: " + resumeId)
        );

        resumeSections.setSectionType(SectionType.valueOf(sectionType));
        resumeSections.setResume(resume);
        return resumeSectionsRepository.save(resumeSections);
    }

    @Override
    public List<ResumeSections> getResumeSections(UUID resumeId, String sectionType) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found with id: " + resumeId)
        );
        if (SectionType.EDUCATION.name().toLowerCase().equals(sectionType)) {
            return resumeSectionsRepository.findByResumeAndSectionType(resume, SectionType.EDUCATION);
        }
        if (SectionType.EXPERIENCE.name().toLowerCase().equals(sectionType)) {
            return resumeSectionsRepository.findByResumeAndSectionType(resume, SectionType.EXPERIENCE);
        }
        if (SectionType.PROJECT.name().toLowerCase().equals(sectionType)) {
            return resumeSectionsRepository.findByResumeAndSectionType(resume, SectionType.PROJECT);
        }
        return resumeSectionsRepository.findByResume(resume);
    }

    @Override
    public void updateResumeSection(ResumeSections resumeSection, UUID resumeId, UUID resumeSectionId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found with id: " + resumeId)
        );
        ResumeSections rs = resumeSectionsRepository.findById(resumeSectionId).orElseThrow(
                () -> new NoSuchElementException("ResumeSection not found with id: " + resumeSectionId)
        );
        if (!StringUtils.isEmpty(resumeSection.getDescription())) rs.setDescription(resumeSection.getDescription());
        if (!StringUtils.isEmpty(resumeSection.getLocation())) rs.setLocation(resumeSection.getLocation());
        if (!StringUtils.isEmpty(resumeSection.getEndDate())) rs.setEndDate(resumeSection.getEndDate());
        if (!StringUtils.isEmpty(resumeSection.getStartDate())) rs.setStartDate(resumeSection.getStartDate());
        if (!StringUtils.isEmpty(resumeSection.getTitle())) rs.setTitle(resumeSection.getTitle());
        if (!StringUtils.isEmpty(resumeSection.getOrganization())) rs.setOrganization(resumeSection.getOrganization());

        rs.setResume(resume);
        resumeSectionsRepository.save(rs);
    }

    @Override
    public void deleteResumeSection(UUID resumeId, UUID resumeSectionId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found with id: " + resumeId)
        );
        ResumeSections rs = resumeSectionsRepository.findById(resumeSectionId).orElseThrow(
                () -> new NoSuchElementException("ResumeSection not found with id: " + resumeSectionId)
        );

        rs.setResume(resume);
        resumeSectionsRepository.delete(rs);
    }
}
