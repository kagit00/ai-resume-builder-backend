package com.ai.resume.builder.services;

import com.ai.resume.builder.dto.LanguageRequest;
import com.ai.resume.builder.dto.LanguageResponse;

import java.util.List;
import java.util.UUID;

public interface LanguageService {
    LanguageResponse saveLanguage(UUID resumeId, LanguageRequest languageRequest);
    List<LanguageResponse> getLanguages(UUID resumeId);
    void updateLanguage(UUID resumeId, UUID languageId, LanguageRequest languageRequest);
    void deleteLanguage(UUID resumeId, UUID languageId);
}
