package com.ai.resume.builder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeSectionRequest {
    private String title;
    private String organization;
    private String startDate;
    private String endDate;
    private String description;
    private String location;
    private String sectionType;
}
