package com.ai.resume.builder.services;

import com.ai.resume.builder.dto.ResumeSectionRequest;
import com.ai.resume.builder.dto.ResumeSectionResponse;
import com.ai.resume.builder.exceptions.BadRequestException;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.ResumeSection;
import com.ai.resume.builder.models.SectionType;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.repository.ResumeSectionsRepository;
import com.ai.resume.builder.utilities.BasicUtility;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import com.ai.resume.builder.utilities.ResponseMakerUtility;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResumeSectionsServiceImplementation implements ResumeSectionsService {
    private final ResumeRepository resumeRepository;
    private final ResumeSectionsRepository resumeSectionsRepository;

    @Override
    @CachePut(value = "resumeSectionCache", key = "#result.id",  unless = "#result == null")
    @CacheEvict(value = "resumeSectionsListCache", allEntries = true)
    public ResumeSectionResponse saveResumeSection(ResumeSectionRequest resumeSectionRequest, UUID resumeId, String sectionType) {
        if (Objects.isNull(resumeSectionRequest) || Objects.isNull(resumeId)) {
            throw new BadRequestException("Resume id or resume section is null");
        }

        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        ResumeSection resumeSection = ResumeSection.builder()
                .title(resumeSectionRequest.getTitle()).organization(resumeSectionRequest.getOrganization())
                .startDate(resumeSectionRequest.getStartDate()).endDate(resumeSectionRequest.getEndDate())
                .description(resumeSectionRequest.getDescription()).location(resumeSectionRequest.getLocation())
                .sectionType(SectionType.valueOf(sectionType)).resume(resume)
                .build();

        resumeSectionsRepository.save(resumeSection);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resume.getResumeSections().add(resumeSection);
        resumeRepository.save(resume);

        return ResponseMakerUtility.getResumeSectionResponse(resumeSection);
    }

    @Override
    @Cacheable(value = "resumeSectionsListCache", key = "#resumeId + '_' + #sectionType")
    public List<ResumeSectionResponse> getResumeSections(UUID resumeId, String sectionType) {
        List<ResumeSection> resumeSections = new ArrayList<>();
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);

        if (SectionType.EDUCATION.name().toLowerCase().equals(sectionType))
            resumeSections =  resumeSectionsRepository.findByResumeAndSectionType(resume, SectionType.EDUCATION);

        if (SectionType.EXPERIENCE.name().toLowerCase().equals(sectionType))
            resumeSections = resumeSectionsRepository.findByResumeAndSectionType(resume, SectionType.EXPERIENCE);

        if (SectionType.PROJECT.name().toLowerCase().equals(sectionType))
            resumeSections = resumeSectionsRepository.findByResumeAndSectionType(resume, SectionType.PROJECT);

        return resumeSections.stream()
                .map(ResponseMakerUtility::getResumeSectionResponse)
                .toList();
    }

    @Override
    @CacheEvict(value = {"resumeSectionCache", "resumeSectionsListCache"}, allEntries = true)
    public void updateResumeSection(ResumeSectionRequest resumeSectionRequest, UUID resumeId, UUID resumeSectionId) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        ResumeSection rs = resumeSectionsRepository.findById(resumeSectionId)
                .orElseThrow(() -> new NoSuchElementException("Resume Section not found"));

        rs.setLocation(resumeSectionRequest.getLocation());
        rs.setDescription(resumeSectionRequest.getDescription());
        rs.setOrganization(resumeSectionRequest.getOrganization());
        rs.setEndDate(resumeSectionRequest.getEndDate());
        rs.setStartDate(resumeSectionRequest.getStartDate());
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
