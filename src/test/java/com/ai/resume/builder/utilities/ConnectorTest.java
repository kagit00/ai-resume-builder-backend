package com.ai.resume.builder.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ConnectorTest {
        @Mock
        private RestTemplate restTemplate;

        private String apiUrl;

        private String apiKey;

        @BeforeEach
        void setUp() {
            apiUrl = "https://api.groq.com/openai/v1/chat/completions";
            apiKey = "0000000000000000000000000000000000000000000"; // this test will be successful if right apikey be in place. Currently, its removed for security purpose
        }

    //@Test
    void performRequest_successfulResponse_returnsNonNullMessageContent() {
        String title = "AI Resume Builder";
        String sectionType = "Experience";
        String expectedResponse = "{\"object\":\"chat.completion\","
                + "\"created\":1733210012,"
                + "\"model\":\"mixtral-8x7b-32768\","
                + "\"choices\":[{\"index\":0,"
                + "\"message\":{\"role\":\"assistant\",\"content\":\"Generated content for AI Resume Builder.\"}}]}";

        lenient().when(restTemplate.exchange(eq(apiUrl), eq(HttpMethod.POST), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        String actualResponse = Connector.performRequest(apiUrl, apiKey, title, sectionType, HttpMethod.POST);

        assertTrue(actualResponse.contains("\"choices\""), "Response should contain 'choices' property");
        assertTrue(actualResponse.contains("\"content\""), "Response should contain 'content' inside 'message'");
    }
}