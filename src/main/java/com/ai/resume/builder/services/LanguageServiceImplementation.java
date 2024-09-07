package com.ai.resume.builder.services;

import com.ai.resume.builder.cache.Cache;
import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.Language;
import com.ai.resume.builder.models.ProficiencyLevel;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.repository.LanguageRepository;
import com.ai.resume.builder.repository.ResumeRepository;
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
    private final Cache cache;

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

        Resume resume = cache.getResumeById(resumeId);

        language.setProficiencyLevel(ProficiencyLevel.valueOf(language.getProficiencyLevel().name()));
        language.setResume(resume);
        languageRepository.save(language);
        return language;
    }

    @Override
    @Cacheable(value = "languagesListCache", key = "#resumeId")
    public List<Language> getLanguages(UUID resumeId) {
        Resume resume = cache.getResumeById(resumeId);
        return languageRepository.findByResume(resume);
    }

    @Override
    @CacheEvict(value = {"languageCache", "languagesListCache"}, allEntries = true)
    public void updateLanguage(UUID resumeId, UUID languageId, Language language) {
        Resume resume = cache.getResumeById(resumeId);
        Language lang = cache.getLanguageById(languageId);

        if (StringUtils.isEmpty(language.getName()))
            throw new InternalServerErrorException("Language name is required");

        if (StringUtils.isEmpty(language.getProficiencyLevel().name()))
            throw new InternalServerErrorException("Language proficiency level is required");

        lang.setName(language.getName());
        lang.setProficiencyLevel(language.getProficiencyLevel());
        lang.setResume(resume);
        languageRepository.save(lang);
    }

    @Override
    @CacheEvict(value = {"languageCache", "languagesListCache"}, allEntries = true)
    public void deleteLanguage(UUID resumeId, UUID languageId) {
        Resume resume = cache.getResumeById(resumeId);
        Language lang = cache.getLanguageById(languageId);
        lang.setResume(resume);
        languageRepository.delete(lang);
    }
}
