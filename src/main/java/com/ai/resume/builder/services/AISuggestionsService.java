package com.ai.resume.builder.services;

import com.ai.resume.builder.models.AISuggestion;

public interface AISuggestionsService {
    AISuggestion generateSuggestions(String resumeTitle, String sectionType);
}
