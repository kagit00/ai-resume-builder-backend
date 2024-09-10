package com.ai.resume.builder.services;

import com.ai.resume.builder.cache.Cache;
import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.ResumeSection;
import com.ai.resume.builder.models.SectionType;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.repository.ResumeSectionsRepository;
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
public class ResumeSectionsServiceImplementation implements ResumeSectionsService {
    private final ResumeRepository resumeRepository;
    private final ResumeSectionsRepository resumeSectionsRepository;
    private final Cache cache;

    @Override
    @CachePut(value = "resumeSectionCache", key = "#result.id",  unless = "#result == null")
    @CacheEvict(value = "resumeSectionsListCache", allEntries = true)
    public ResumeSection saveResumeSection(ResumeSection resumeSection, UUID resumeId, String sectionType) {
        if (Objects.isNull(resumeSection) || Objects.isNull(resumeId)) {
            throw new InternalServerErrorException("Resume id or resume section is null");
        }
        Resume resume = cache.getResumeById(resumeId);

        resumeSection.setSectionType(SectionType.valueOf(sectionType));
        resumeSection.setResume(resume);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());

        resumeSectionsRepository.save(resumeSection);
        resumeRepository.save(resume);
        return resumeSection;
    }

    @Override
    @Cacheable(value = "resumeSectionsListCache", key = "#resumeId + '_' + #sectionType")
    public List<ResumeSection> getResumeSections(UUID resumeId, String sectionType) {
        Resume resume = cache.getResumeById(resumeId);

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
        Resume resume = cache.getResumeById(resumeId);
        ResumeSection rs = cache.getResumeSectionById(resumeSectionId);

        if (!StringUtils.isEmpty(resumeSection.getDescription())) rs.setDescription(resumeSection.getDescription());
        if (!StringUtils.isEmpty(resumeSection.getLocation())) rs.setLocation(resumeSection.getLocation());
        if (!StringUtils.isEmpty(resumeSection.getEndDate())) rs.setEndDate(resumeSection.getEndDate());
        if (!StringUtils.isEmpty(resumeSection.getStartDate())) rs.setStartDate(resumeSection.getStartDate());
        if (!StringUtils.isEmpty(resumeSection.getTitle())) rs.setTitle(resumeSection.getTitle());
        if (!StringUtils.isEmpty(resumeSection.getOrganization())) rs.setOrganization(resumeSection.getOrganization());

        rs.setResume(resume);
        resumeSectionsRepository.save(rs);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resumeRepository.save(resume);
    }

    @Override
    @CacheEvict(value = {"resumeSectionCache", "resumeSectionsListCache"}, allEntries = true)
    public void deleteResumeSection(UUID resumeId, UUID resumeSectionId) {
        Resume resume = cache.getResumeById(resumeId);
        ResumeSection rs = resumeSectionsRepository.findById(resumeSectionId).orElseThrow(
                () -> new NoSuchElementException("ResumeSection not found with id: " + resumeSectionId)
        );

        rs.setResume(resume);
        resumeSectionsRepository.delete(rs);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resumeRepository.save(resume);
    }
}
