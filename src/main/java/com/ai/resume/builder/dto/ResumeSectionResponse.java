package com.ai.resume.builder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeSectionResponse {
    private UUID id;
    private String title;
    private String organization;
    private String startDate;
    private String endDate;
    private String description;
    private String location;
    private String sectionType;
}
