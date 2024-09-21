package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.ResumeSection;
import com.ai.resume.builder.models.SectionType;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.repository.ResumeSectionsRepository;
import com.ai.resume.builder.utilities.BasicUtility;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ResumeSectionsServiceImplementation implements ResumeSectionsService {
    private final ResumeRepository resumeRepository;
    private final ResumeSectionsRepository resumeSectionsRepository;

    @Override
    @CachePut(value = "resumeSectionCache", key = "#result.id",  unless = "#result == null")
    @CacheEvict(value = "resumeSectionsListCache", allEntries = true)
    public ResumeSection saveResumeSection(ResumeSection resumeSection, UUID resumeId, String sectionType) {
        if (Objects.isNull(resumeSection) || Objects.isNull(resumeId)) {
            throw new InternalServerErrorException("Resume id or resume section is null");
        }

        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        resumeSection.setSectionType(SectionType.valueOf(sectionType));
        resumeSection.setResume(resume);
        resumeSectionsRepository.save(resumeSection);

        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resume.getResumeSections().add(resumeSection);
        resumeRepository.save(resume);
        return resumeSection;
    }

    @Override
    @Cacheable(value = "resumeSectionsListCache", key = "#resumeId + '_' + #sectionType")
    public List<ResumeSection> getResumeSections(UUID resumeId, String sectionType) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);

        if (SectionType.EDUCATION.name().toLowerCase().equals(sectionType))
            return resumeSectionsRepository.findByResumeAndSectionType(resume, SectionType.EDUCATION);

        if (SectionType.EXPERIENCE.name().toLowerCase().equals(sectionType))
            return resumeSectionsRepository.findByResumeAndSectionType(resume, SectionType.EXPERIENCE);

        if (SectionType.PROJECT.name().toLowerCase().equals(sectionType))
            return resumeSectionsRepository.findByResumeAndSectionType(resume, SectionType.PROJECT);

        return resumeSectionsRepository.findByResume(resume);
    }

    @Override
    @CacheEvict(value = {"resumeSectionCache", "resumeSectionsListCache"}, allEntries = true)
    public void updateResumeSection(ResumeSection resumeSection, UUID resumeId, UUID resumeSectionId) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        ResumeSection rs = resumeSectionsRepository.findById(resumeSectionId).orElseThrow();

        rs.setLocation(resumeSection.getLocation());
        rs.setDescription(resumeSection.getDescription());
        rs.setOrganization(resumeSection.getOrganization());
        rs.setEndDate(resumeSection.getEndDate());
        rs.setStartDate(resumeSection.getStartDate());
        rs.setResume(resume);

        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        rs.setResume(resume);
        resumeRepository.save(resume);
    }

    @Override
    @CacheEvict(value = {"resumeSectionCache", "resumeSectionsListCache"}, allEntries = true)
    public void deleteResumeSection(UUID resumeId, UUID resumeSectionId) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);

        ResumeSection rs = resumeSectionsRepository.findById(resumeSectionId).orElseThrow(
                () -> new NoSuchElementException("ResumeSection not found")
        );

        resume.getResumeSections().remove(rs);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resumeRepository.save(resume);
    }
}
