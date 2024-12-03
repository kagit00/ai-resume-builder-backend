package com.ai.resume.builder.dto;

import jakarta.validation.constraints.NotBlank;
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
public class ResumeSectionRequest {
    @Pattern(
            regexp = "^[A-Za-z0-9 .,&():-]+$",
            message = "Title can only contain alphabets, numbers, spaces, and common punctuation (.,&():-)."
    )
    private String title;
    @NotNull
    @NotBlank
    private String organization;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Start date must be in the format YYYY-MM-DD.")
    private String startDate;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Start date must be in the format YYYY-MM-DD.")
    private String endDate;
    private String description;
    @NotNull
    @NotBlank
    private String location;
    private String sectionType;
}
