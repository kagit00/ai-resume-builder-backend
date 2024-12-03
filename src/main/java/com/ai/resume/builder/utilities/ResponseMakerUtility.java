package com.ai.resume.builder.utilities;

import com.ai.resume.builder.dto.AdditionalDetailsResponse;
import com.ai.resume.builder.dto.LanguageResponse;
import com.ai.resume.builder.dto.ResumeSectionResponse;
import com.ai.resume.builder.dto.UserResponse;
import com.ai.resume.builder.models.AdditionalDetails;
import com.ai.resume.builder.models.Language;
import com.ai.resume.builder.models.ResumeSection;
import com.ai.resume.builder.models.User;

import java.util.UUID;

public final class ResponseMakerUtility {

    private ResponseMakerUtility() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public static UserResponse getUserResponse(User user) {
        return UserResponse.builder()
                .bio(user.getBio()).name(user.getName()).username(user.getUsername()).password(user.getPassword())
                .resumes(user.getResumes()).roles(user.getRoles())
                .build();
    }

    public static AdditionalDetailsResponse getAdditionalDetailsResponse(AdditionalDetails additionalDetails) {
        return AdditionalDetailsResponse.builder()
                .id(additionalDetails.getId()).githubLink(additionalDetails.getGithubLink())
                .phoneNumber(additionalDetails.getPhoneNumber()).linkedInProfileLink(additionalDetails.getLinkedInProfileLink())
                .build();
    }

    public static LanguageResponse getLanguageResponse(Language language) {
        return LanguageResponse.builder()
                .id(language.getId()).name(language.getName()).proficiencyLevel(language.getProficiencyLevel())
                .build();
    }

    public static ResumeSectionResponse getResumeSectionResponse(ResumeSection resumeSection) {
        return ResumeSectionResponse.builder()
                .id(resumeSection.getId()).title(resumeSection.getTitle()).organization(resumeSection.getOrganization())
                .startDate(resumeSection.getStartDate()).endDate(resumeSection.getEndDate()).description(resumeSection.getDescription())
                .location(resumeSection.getLocation()).sectionType(resumeSection.getSectionType().name())
                .build();
    }
}
