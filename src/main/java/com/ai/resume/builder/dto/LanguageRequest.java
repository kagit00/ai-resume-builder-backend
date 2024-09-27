package com.ai.resume.builder.dto;

import com.ai.resume.builder.models.ProficiencyLevel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LanguageRequest {

    @NotNull(message = "Language name cannot be null")
    @Pattern(regexp = "^[a-zA-Z ]{1,50}$", message = "Invalid language name")
    private String name;

    @NotNull(message = "Proficiency level cannot be null")
    private ProficiencyLevel proficiencyLevel;
}
