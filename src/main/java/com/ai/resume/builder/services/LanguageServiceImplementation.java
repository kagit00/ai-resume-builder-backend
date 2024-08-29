package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.Language;
import com.ai.resume.builder.models.ProficiencyLevel;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.repository.LanguageRepository;
import com.ai.resume.builder.repository.ResumeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LanguageServiceImplementation implements LanguageService {
    private final ResumeRepository resumeRepository;
    private final LanguageRepository languageRepository;

    @Override
    public void saveLanguage(Language language, UUID resumeId) {
        if (Objects.isNull(language.getName()) || Objects.isNull(resumeId) ) {
            throw new InternalServerErrorException("Language & resume id cannot be null");
        }

        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found with id: " + resumeId)
        );

        language.setResume(resume);
        language.setProficiencyLevel(ProficiencyLevel.valueOf(language.getProficiencyLevel().name()));
        languageRepository.save(language);
    }

    @Override
    public List<Language> getLanguages(UUID resumeId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found with id: " + resumeId)
        );

        return languageRepository.findByResume(resume);
    }
}
