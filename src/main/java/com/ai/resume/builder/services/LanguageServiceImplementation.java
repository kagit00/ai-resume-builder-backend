package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.Language;
import com.ai.resume.builder.models.ProficiencyLevel;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.repository.LanguageRepository;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.utilities.BasicUtility;
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
public class LanguageServiceImplementation implements LanguageService {
    private final ResumeRepository resumeRepository;
    private final LanguageRepository languageRepository;

    @Override
    @CachePut(value = "languageCache", key = "#result.id", unless = "#result == null")
    @CacheEvict(value = "languagesListCache", allEntries = true)
    public Language saveLanguage(UUID resumeId, Language language) {
        if (Objects.isNull(resumeId) && Objects.isNull(language))
            throw new InternalServerErrorException("Resume id and language is null.");

        if (StringUtils.isEmpty(language.getName()))
            throw new InternalServerErrorException("Language name is required");

        if (StringUtils.isEmpty(language.getProficiencyLevel().name()))
            throw new InternalServerErrorException("Language proficiency level is required");

        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);

        language.setProficiencyLevel(ProficiencyLevel.valueOf(language.getProficiencyLevel().name()));
        language.setResume(resume);
        languageRepository.save(language);

        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resume.getLanguages().add(language);
        resumeRepository.save(resume);
        return language;
    }

    @Override
    @Cacheable(value = "languagesListCache", key = "#resumeId")
    public List<Language> getLanguages(UUID resumeId) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        return languageRepository.findByResume(resume);
    }

    @Override
    @CacheEvict(value = {"languageCache", "languagesListCache"}, allEntries = true)
    public void updateLanguage(UUID resumeId, UUID languageId, Language language) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        Language lang = languageRepository.findById(languageId).orElseThrow();

        lang.setName(language.getName());
        lang.setProficiencyLevel(language.getProficiencyLevel());
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
