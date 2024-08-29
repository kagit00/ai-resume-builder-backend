package com.ai.resume.builder.services;

import com.ai.resume.builder.models.Language;

import java.util.List;
import java.util.UUID;

public interface LanguageService {
    void saveLanguage(Language language, UUID resumeId);
    List<Language> getLanguages(UUID resumeId);
}
