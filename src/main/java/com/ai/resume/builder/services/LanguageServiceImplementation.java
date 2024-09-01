package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.Language;
import com.ai.resume.builder.models.ProficiencyLevel;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.repository.LanguageRepository;
import com.ai.resume.builder.repository.ResumeRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class LanguageServiceImplementation implements LanguageService {
    private final ResumeRepository resumeRepository;
    private final LanguageRepository languageRepository;

    @Override
    public Language saveLanguage(UUID resumeId, Language language) {
        if (Objects.isNull(resumeId) && Objects.isNull(language)) throw new InternalServerErrorException("Resume id and language is null.");
        if (StringUtils.isEmpty(language.getName())) throw new InternalServerErrorException("Language name is required");
        if (StringUtils.isEmpty(language.getProficiencyLevel().name())) throw new InternalServerErrorException("Language proficiency level is required");

        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found with id: " + resumeId)
        );
        language.setProficiencyLevel(ProficiencyLevel.valueOf(language.getProficiencyLevel().name()));
        language.setResume(resume);
        languageRepository.save(language);
        return language;
    }

    @Override
    public List<Language> getLanguages(UUID resumeId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found with id: " + resumeId)
        );
        return languageRepository.findByResume(resume);
    }

    @Override
    public void updateLanguage(UUID resumeId, UUID languageId, Language language) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found with id: " + resumeId)
        );
        Language lang = languageRepository.findById(languageId).orElseThrow(
                () -> new NoSuchElementException("Language not found with id: " + languageId)
        );
        if (StringUtils.isEmpty(language.getName())) throw new InternalServerErrorException("Language name is required");
        if (StringUtils.isEmpty(language.getProficiencyLevel().name())) throw new InternalServerErrorException("Language proficiency level is required");
        lang.setName(language.getName());
        lang.setProficiencyLevel(language.getProficiencyLevel());
        lang.setResume(resume);
        languageRepository.save(lang);
    }

    @Override
    public void deleteLanguage(UUID resumeId, UUID languageId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found with id: " + resumeId)
        );
        Language lang = languageRepository.findById(languageId).orElseThrow(
                () -> new NoSuchElementException("Language not found with id: " + languageId)
        );
        lang.setResume(resume);
        languageRepository.delete(lang);
    }
}
