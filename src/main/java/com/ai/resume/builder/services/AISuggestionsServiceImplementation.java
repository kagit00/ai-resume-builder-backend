package com.ai.resume.builder.services;

import com.ai.resume.builder.models.AISuggestion;
import com.ai.resume.builder.utilities.Connector;
import com.ai.resume.builder.utilities.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AISuggestionsServiceImplementation implements AISuggestionsService {

    @Value("${groq.api.url}")
    private String groqUrl;
    @Value("${groq.api.key}")
    private String groqApiKey;
    private final AISuggestion aiSuggestion = new AISuggestion();
    private static final Logger log = LoggerFactory.getLogger(AISuggestionsServiceImplementation.class);
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public AISuggestion generateSuggestions(String title, String sectionType) {
        String generatedText = "";

        // Call the Groq API with the updated method
        String response = Connector.postRequest(groqUrl, groqApiKey, title, sectionType);
        generatedText = parseResponse(response);

        aiSuggestion.setGeneratedSuggestion(generatedText);
        return aiSuggestion;
    }

    private String parseResponse(String rawResponse) {
        String beautifiedResponse = "";
        if (Objects.isNull(rawResponse) || rawResponse.isEmpty()) return "No response received from the API.";

        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            if (root.has(Constant.CHOICES) && root.get(Constant.CHOICES).isArray()) {
                beautifiedResponse = root.get("choices").get(0).get("message").get("content").asText();
            } else {
                return "Unexpected response format or empty array.";
            }
        } catch (JsonProcessingException e) {
            return "Failed to parse the API response: " + e.getMessage();
        }
        return beautifiedResponse;
    }
}
