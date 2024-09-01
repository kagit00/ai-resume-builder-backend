package com.ai.resume.builder.services;

import com.ai.resume.builder.models.Language;

import java.util.List;
import java.util.UUID;

public interface LanguageService {
    Language saveLanguage(UUID resumeId, Language language);
    List<Language> getLanguages(UUID resumeId);
    void updateLanguage(UUID resumeId, UUID languageId, Language language);
    void deleteLanguage(UUID resumeId, UUID languageId);
}
