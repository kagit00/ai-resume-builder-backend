package com.ai.resume.builder.utilities;

import com.ai.resume.builder.exceptions.BadRequestException;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.repository.ResumeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BasicUtilityTest {

    @Mock
    private ResumeRepository resumeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void stringifyObject_validObject_returnsJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TestObject testObject = new TestObject("John", 25);
        String expectedJson = mapper.writeValueAsString(testObject);


        String result = BasicUtility.stringifyObject(testObject);
        assertEquals(expectedJson, result);
    }

    @Test
    void stringifyObject_invalidObject_throwsBadRequestException() {
        Object invalidObject = new Object() {
            private final Object circularReference = this;
        };
        assertThrows(BadRequestException.class, () -> BasicUtility.stringifyObject(invalidObject));
    }

    @Test
    void readSpecificProperty_validJsonAndProp_returnsPropertyValue() {
        String json = "{\"name\":\"John\", \"age\":25}";
        String prop = "$.name";

        String result = BasicUtility.readSpecificProperty(json, prop);
        assertEquals("John", result);
    }

    @Test
    void readSpecificProperty_invalidJsonOrProp_returnsEmptyString() {
        String invalidJson = "{name:\"John\", age:25}";
        String invalidProp = "$.nonexistent";

        String result = BasicUtility.readSpecificProperty(invalidJson, invalidProp);
        assertEquals("", result);
    }

    @Test
    void getResumeById_validId_returnsResume() {
        UUID resumeId = UUID.randomUUID();
        Resume mockResume = new Resume();
        mockResume.setId(resumeId);
        mockResume.setTitle("Sample Resume");
        when(resumeRepository.findById(resumeId)).thenReturn(Optional.of(mockResume));

        Resume result = BasicUtility.getResumeById(resumeId, resumeRepository);

        assertNotNull(result, "Resume should not be null");
        assertEquals(mockResume, result, "Retrieved resume should match the mock resume");
        verify(resumeRepository, times(1)).findById(resumeId);
    }

    @Test
    void getResumeById_invalidId_throwsNoSuchElementException() {
        UUID resumeId = UUID.randomUUID();
        when(resumeRepository.findById(resumeId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> BasicUtility.getResumeById(resumeId, resumeRepository));

        String expectedMessage = "Resume not found for id: " + resumeId;
        assertTrue(exception.getMessage().contains(expectedMessage),
                "Expected exception message to contain: " + expectedMessage);
    }


    @Test
    void getDomainFromUrl_validUrl_returnsDomain() {
        String urlString = "https://www.example.com/path?query=123";
        String result = BasicUtility.getDomainFromUrl(urlString);
        assertEquals("www.example.com", result);
    }

    @Test
    void getDomainFromUrl_invalidUrl_returnsNull() {
        String invalidUrl = "htp:/invalid-url";
        String result = BasicUtility.getDomainFromUrl(invalidUrl);
        assertNull(result);
    }

    @Getter
    @AllArgsConstructor
    private static class TestObject {
        private final String name;
        private final int age;
    }
}