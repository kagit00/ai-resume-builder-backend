package com.ai.resume.builder.services;

import com.ai.resume.builder.cache.Cache;
import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.AdditionalDetails;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.repository.AdditionalDetailsRepository;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.utilities.BasicUtility;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
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
    public AdditionalDetails saveAdditionalDetails(AdditionalDetails additionalDetails, UUID resumeId) {
        if (Objects.isNull(additionalDetails) || Objects.isNull(resumeId))
            throw new InternalServerErrorException("Additional details or resume id is null");

        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);

        additionalDetails.setResume(resume);
        resume.setAdditionalDetails(additionalDetails);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());

        additionalDetailsRepository.save(additionalDetails);
        resumeRepository.save(resume);
        return additionalDetails;
    }

    @Override
    @Cacheable(value = "additionalDetailsCache", key = "#resumeId")
    public AdditionalDetails getAdditionalDetails(UUID resumeId) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);
        return additionalDetailsRepository.findByResume(resume);
    }

    @Override
    @CachePut(value = "additionalDetailsCache", key = "#resumeId", unless = "#result == null")
    public AdditionalDetails updateAdditionalDetails(AdditionalDetails additionalDetails, UUID resumeId, UUID additionalDetailsId) {
        Resume resume = BasicUtility.getResumeById(resumeId, resumeRepository);

        AdditionalDetails ad = additionalDetailsRepository.findByResume(resume);
        if (!StringUtils.isEmpty(additionalDetails.getGithubLink())) ad.setGithubLink(additionalDetails.getGithubLink());
        if (!StringUtils.isEmpty(additionalDetails.getPhoneNumber())) ad.setPhoneNumber(additionalDetails.getPhoneNumber());
        if (!StringUtils.isEmpty(additionalDetails.getLinkedInProfileLink())) ad.setLinkedInProfileLink(additionalDetails.getLinkedInProfileLink());

        ad.setResume(resume);
        resume.setAdditionalDetails(ad);
        resume.setUpdatedAt(DefaultValuesPopulator.getCurrentTimestamp());

        additionalDetailsRepository.save(ad);
        resumeRepository.save(resume);
        return ad;
    }
}
