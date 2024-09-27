package com.ai.resume.builder.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalDetailsResponse {
    private UUID id;
    private String githubLink;
    private String phoneNumber;
    private String linkedInProfileLink;
}
