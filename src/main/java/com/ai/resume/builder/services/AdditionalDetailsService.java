package com.ai.resume.builder.services;

import com.ai.resume.builder.dto.AdditionalDetailsRequest;
import com.ai.resume.builder.dto.AdditionalDetailsResponse;

import java.util.UUID;

public interface AdditionalDetailsService {
    AdditionalDetailsResponse saveAdditionalDetails(AdditionalDetailsRequest additionalDetailsRequest, UUID resumeId);
    AdditionalDetailsResponse getAdditionalDetails(UUID resumeId);
    AdditionalDetailsResponse updateAdditionalDetails(AdditionalDetailsRequest additionalDetailsRequest, UUID resumeId, UUID additionalDetailsId);
}
