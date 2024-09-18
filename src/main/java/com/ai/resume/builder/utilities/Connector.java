package com.ai.resume.builder.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Connector {
    private static final Logger log = LoggerFactory.getLogger(Connector.class);

    private Connector() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public static String postRequest(String url, String apiKey, String title, String sectionType) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // Build the request payload for Groq API
        HttpEntity<Map<String, Object>> entity = getMapHttpEntity(title, sectionType, headers);

        // Make the POST request
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        String rawResponse = response.getBody();
        log.info("Raw Response: {}", rawResponse);

        return rawResponse;
    }

    private static HttpEntity<Map<String, Object>> getMapHttpEntity(String title, String sectionType, HttpHeaders headers) {
        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", "Generate " + sectionType + " for a resume titled: " + title);
        messages.add(message);

        // Specify the model
        requestBody.put("messages", messages);
        requestBody.put("model", "mixtral-8x7b-32768");

        // Wrap the request body and headers in an HttpEntity
        return new HttpEntity<>(requestBody, headers);
    }
}
