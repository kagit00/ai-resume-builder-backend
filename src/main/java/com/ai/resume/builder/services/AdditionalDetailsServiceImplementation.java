package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.AdditionalDetails;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.repository.AdditionalDetailsRepository;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdditionalDetailsServiceImplementation implements AdditionalDetailsService {
    private final ResumeRepository resumeRepository;
    private final AdditionalDetailsRepository additionalDetailsRepository;

    @Override
    public AdditionalDetails saveAdditionalDetails(AdditionalDetails additionalDetails, UUID resumeId) {
        if (Objects.isNull(additionalDetails) || Objects.isNull(resumeId)) {
            throw new InternalServerErrorException("Additional details or resume id is null");
        }
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found for id: " + resumeId)
        );
        additionalDetails.setResume(resume);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        additionalDetailsRepository.save(additionalDetails);
        resumeRepository.save(resume);
        return additionalDetails;
    }

    @Override
    public AdditionalDetails getAdditionalDetails(UUID resumeId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found for id: " + resumeId)
        );
        return additionalDetailsRepository.findByResume(resume);
    }

    @Override
    public void updateAdditionalDetails(AdditionalDetails additionalDetails, UUID resumeId, UUID additionalDetailsId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found for id: " + resumeId)
        );

        AdditionalDetails ad = additionalDetailsRepository.findByResume(resume);
        if (!StringUtils.isEmpty(additionalDetails.getGithubLink())) ad.setGithubLink(additionalDetails.getGithubLink());
        if (!StringUtils.isEmpty(additionalDetails.getPhoneNumber())) ad.setPhoneNumber(additionalDetails.getPhoneNumber());
        if (!StringUtils.isEmpty(additionalDetails.getLinkedInProfileLink())) ad.setLinkedInProfileLink(additionalDetails.getLinkedInProfileLink());

        ad.setResume(resume);
        additionalDetailsRepository.save(ad);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());
        resumeRepository.save(resume);
    }
}
