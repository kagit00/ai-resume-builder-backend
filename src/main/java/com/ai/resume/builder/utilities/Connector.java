package com.ai.resume.builder.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
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

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("inputs", title + " - " + sectionType);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        String rawResponse = response.getBody();
        log.debug("Raw Response: {}", rawResponse);

        return rawResponse;
    }
}
