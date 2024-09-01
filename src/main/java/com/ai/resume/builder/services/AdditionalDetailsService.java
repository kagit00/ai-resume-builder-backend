package com.ai.resume.builder.services;

import com.ai.resume.builder.models.AdditionalDetails;

import java.util.UUID;

public interface AdditionalDetailsService {
    AdditionalDetails saveAdditionalDetails(AdditionalDetails additionalDetails, UUID resumeId);
    AdditionalDetails getAdditionalDetails(UUID resumeId);
    void updateAdditionalDetails(AdditionalDetails additionalDetails, UUID resumeId, UUID additionalDetailsId);
}
