package com.ai.resume.builder.dto;

import com.ai.resume.builder.models.ProficiencyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageResponse {
    private UUID id;
    private String name;
    private ProficiencyLevel proficiencyLevel;
}
