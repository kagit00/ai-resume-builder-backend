package com.ai.resume.builder.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ResumeSummaryResponse {
    private UUID id;
    private String details;
}