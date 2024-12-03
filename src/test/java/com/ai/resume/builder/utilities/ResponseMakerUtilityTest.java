package com.ai.resume.builder.utilities;

import com.ai.resume.builder.dto.AdditionalDetailsResponse;
import com.ai.resume.builder.dto.LanguageResponse;
import com.ai.resume.builder.dto.ResumeSectionResponse;
import com.ai.resume.builder.dto.UserResponse;
import com.ai.resume.builder.models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;
import java.util.UUID;


class ResponseMakerUtilityTest {

    @Test
    void getUserResponse_shouldReturnValidResponse() {
        // Arrange
        User user = User.builder()
                .bio("Test bio").name("Test Name").username("testuser").password("password123")
                .resumes(List.of(new Resume())).roles(Set.of(Role.builder().name(Constant.FREE_USER).build()))
                .build();

        // Act
        UserResponse userResponse = ResponseMakerUtility.getUserResponse(user);

        // Assert
        Assertions.assertNotNull(userResponse);
        Assertions.assertEquals("Test bio", userResponse.getBio());
        Assertions.assertEquals("Test Name", userResponse.getName());
        Assertions.assertEquals("testuser", userResponse.getUsername());
        Assertions.assertEquals("password123", userResponse.getPassword());
    }

    @Test
    void getAdditionalDetailsResponse_shouldReturnValidResponse() {
        // Arrange
        AdditionalDetails additionalDetails = AdditionalDetails.builder()
                .id(UUID.fromString(DefaultValuesPopulator.getUid())).githubLink("https://github.com/test")
                .phoneNumber("1234567890").linkedInProfileLink("https://linkedin.com/in/test")
                .build();

        // Act
        AdditionalDetailsResponse response = ResponseMakerUtility.getAdditionalDetailsResponse(additionalDetails);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("https://github.com/test", response.getGithubLink());
        Assertions.assertEquals("1234567890", response.getPhoneNumber());
        Assertions.assertEquals("https://linkedin.com/in/test", response.getLinkedInProfileLink());
    }

    @Test
    void getLanguageResponse_shouldReturnValidResponse() {
        // Arrange
        Language language = Language.builder()
                .id(UUID.fromString(DefaultValuesPopulator.getUid()))
                .name("English").proficiencyLevel(ProficiencyLevel.FLUENT)
                .build();

        // Act
        LanguageResponse response = ResponseMakerUtility.getLanguageResponse(language);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("English", response.getName());
        Assertions.assertEquals(ProficiencyLevel.FLUENT, response.getProficiencyLevel());
    }

    @Test
    void getResumeSectionResponse_shouldReturnValidResponse() {
        // Arrange
        ResumeSection resumeSection = ResumeSection.builder()
                .id(UUID.fromString(DefaultValuesPopulator.getUid())).title("Software Engineer")
                .organization("Test Organization").startDate("2020-01-01").endDate("2023-01-01")
                .description("Developed applications").location("Remote")
                .sectionType(SectionType.EXPERIENCE)
                .build();

        // Act
        ResumeSectionResponse response = ResponseMakerUtility.getResumeSectionResponse(resumeSection);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Software Engineer", response.getTitle());
        Assertions.assertEquals("Test Organization", response.getOrganization());
        Assertions.assertEquals("2020-01-01", response.getStartDate());
        Assertions.assertEquals("2023-01-01", response.getEndDate());
        Assertions.assertEquals("Developed applications", response.getDescription());
        Assertions.assertEquals("Remote", response.getLocation());
        Assertions.assertEquals(SectionType.EXPERIENCE.name(), response.getSectionType());
    }
}