package com.ai.resume.builder.services;

import com.ai.resume.builder.dto.LanguageRequest;
import com.ai.resume.builder.dto.LanguageResponse;
import com.ai.resume.builder.exceptions.BadRequestException;
import com.ai.resume.builder.models.Language;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.repository.LanguageRepository;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.utilities.BasicUtility;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import com.ai.resume.builder.utilities.ResponseMakerUtility;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@AllArgsConstructor
public class LanguageServiceImplementation implements LanguageService {
    private final ResumeRepository resumeRepository;
    private final LanguageRepository languageRepository;

    @Override
    @CachePut(value = "languageCache", key = "#result.id", unless = "#result == null")
    @CacheEvict(value = "languagesListCache", allEntries = true)
    public LanguageResponse saveLanguage(UUID resumeId, LanguageRequest languageRequest) {
        if (Objects.isNull(resumeId) && Objects.isNull(languageRequest)) {
            throw new BadRequestException("Resume id and language should not be empty.");
        }

        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);

        Language language = Language.builder().name(languageRequest.getName()).proficiencyLevel(languageRequest.getProficiencyLevel())
                .resume(BasicUtility.getResumeById(resumeId, resumeRepository))
                .build();

        languageRepository.save(language);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resume.getLanguages().add(language);
        resumeRepository.save(resume);

        return ResponseMakerUtility.getLanguageResponse(language);
    }

    @Override
    @Cacheable(value = "languagesListCache", key = "#resumeId")
    public List<LanguageResponse> getLanguages(UUID resumeId) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        List<Language> languages = languageRepository.findByResume(resume);

        return languages.stream()
                .map(ResponseMakerUtility::getLanguageResponse)
                .toList();
    }

    @Override
    @CacheEvict(value = {"languageCache", "languagesListCache"}, allEntries = true)
    public void updateLanguage(UUID resumeId, UUID languageId, LanguageRequest languageRequest) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        Language lang = languageRepository.findById(languageId).orElseThrow();

        lang.setName(languageRequest.getName());
        lang.setProficiencyLevel(languageRequest.getProficiencyLevel());
        lang.setResume(resume);

        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resumeRepository.save(resume);
    }

    @Override
    @CacheEvict(value = {"languageCache", "languagesListCache"}, allEntries = true)
    public void deleteLanguage(UUID resumeId, UUID languageId) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        Language lang = languageRepository.findById(languageId).orElseThrow();
        resume.getLanguages().remove(lang);
        lang.setResume(null);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resumeRepository.save(resume);
    }
}
