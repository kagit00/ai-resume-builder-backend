package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.AdditionalDetails;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.repository.AdditionalDetailsRepository;
import com.ai.resume.builder.repository.ResumeRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdditionalDetailsServiceImplementation implements AdditionalDetailsService {
    private final ResumeRepository resumeRepository;
    private final AdditionalDetailsRepository additionalDetailsRepository;

    @Override
    public void saveAdditionalDetails(AdditionalDetails additionalDetails, UUID resumeId) {
        if (Objects.isNull(additionalDetails) || Objects.isNull(resumeId)) {
            throw new InternalServerErrorException("Additional details or resume id is null");
        }

        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found for id: " + resumeId)
        );

        additionalDetails.setResume(resume);
        additionalDetailsRepository.save(additionalDetails);
    }

    @Override
    public AdditionalDetails getAdditionalDetails(UUID resumeId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NoSuchElementException("Resume not found for id: " + resumeId)
        );

        return additionalDetailsRepository.findByResume(resume);
    }
}
