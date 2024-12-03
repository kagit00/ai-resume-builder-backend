package com.ai.resume.builder.services;

import com.ai.resume.builder.cache.Cache;
import com.ai.resume.builder.exceptions.BadRequestException;
import com.ai.resume.builder.models.AdditionalDetails;
import com.ai.resume.builder.dto.AdditionalDetailsRequest;
import com.ai.resume.builder.dto.AdditionalDetailsResponse;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.repository.AdditionalDetailsRepository;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.utilities.BasicUtility;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import com.ai.resume.builder.utilities.ResponseMakerUtility;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdditionalDetailsServiceImplementation implements AdditionalDetailsService {
    private final ResumeRepository resumeRepository;
    private final AdditionalDetailsRepository additionalDetailsRepository;
    private final Cache cache;

    @Override
    @CachePut(value = "additionalDetailsCache", key = "#resumeId", unless = "#result == null")
    public AdditionalDetailsResponse saveAdditionalDetails(AdditionalDetailsRequest additionalDetailsRequest, UUID resumeId) {

        if (Objects.isNull(additionalDetailsRequest)) {
            throw new BadRequestException("Additional Details Should've Valid Value");
        }

        if (Objects.isNull(resumeId)) {
            throw new BadRequestException("Resume Id is Not Valid");
        }

        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);

        AdditionalDetails additionalDetails = AdditionalDetails.builder()
                .githubLink(additionalDetailsRequest.getGithubLink()).phoneNumber(additionalDetailsRequest.getPhoneNumber())
                .linkedInProfileLink(additionalDetailsRequest.getLinkedInProfileLink()).resume(resume)
                .build();

        resume.setAdditionalDetails(additionalDetails);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());

        additionalDetailsRepository.save(additionalDetails);
        resumeRepository.save(resume);

        return ResponseMakerUtility.getAdditionalDetailsResponse(additionalDetails);
    }

    @Override
    @Cacheable(value = "additionalDetailsCache", key = "#resumeId")
    public AdditionalDetailsResponse getAdditionalDetails(UUID resumeId) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        AdditionalDetails additionalDetails = additionalDetailsRepository.findByResume(resume);
        if (Objects.isNull(additionalDetails)) {
            return AdditionalDetailsResponse.builder().build();
        }
        return ResponseMakerUtility.getAdditionalDetailsResponse(additionalDetails);
    }

    @Override
    @CacheEvict(value = "additionalDetailsCache", allEntries = true)
    public AdditionalDetailsResponse updateAdditionalDetails(AdditionalDetailsRequest additionalDetailsRequest, UUID resumeId, UUID additionalDetailsId) {
        if (Objects.isNull(additionalDetailsRequest)) {
            throw new BadRequestException("Additional Details Should've Valid Value");
        }

        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        AdditionalDetails additionalDetails = additionalDetailsRepository.findByResume(resume);

        if (Objects.isNull(additionalDetails)) {
            throw new NoSuchElementException("Additional Details not found");
        }

        additionalDetails.setGithubLink(additionalDetailsRequest.getGithubLink());
        additionalDetails.setPhoneNumber(additionalDetailsRequest.getPhoneNumber());
        additionalDetails.setLinkedInProfileLink(additionalDetailsRequest.getLinkedInProfileLink());
        additionalDetails.setResume(resume);

        resume.setAdditionalDetails(additionalDetails);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());

        additionalDetailsRepository.save(additionalDetails);
        resumeRepository.save(resume);

        return ResponseMakerUtility.getAdditionalDetailsResponse(additionalDetails);
    }
}
