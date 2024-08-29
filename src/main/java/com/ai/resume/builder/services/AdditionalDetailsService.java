package com.ai.resume.builder.services;

import com.ai.resume.builder.models.AdditionalDetails;

import java.util.UUID;

public interface AdditionalDetailsService {
    void saveAdditionalDetails(AdditionalDetails additionalDetails, UUID resumeId);
    AdditionalDetails getAdditionalDetails(UUID resumeId);
}
