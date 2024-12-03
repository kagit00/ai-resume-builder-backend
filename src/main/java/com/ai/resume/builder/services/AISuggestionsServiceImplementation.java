package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.AISuggestion;
import com.ai.resume.builder.utilities.Connector;
import com.ai.resume.builder.utilities.Constant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class AISuggestionsServiceImplementation implements AISuggestionsService {
    private final Environment environment;
    private final ObjectMapper objectMapper;


    @Override
    public AISuggestion generateSuggestions(String title, String sectionType) {
        String generatedText;
        HttpMethod methodType = HttpMethod.POST;
        String groqUrl = environment.getProperty("groq.api.url");
        String groqApiKey = environment.getProperty("groq.api.key");

        String response = Connector.performRequest(groqUrl, groqApiKey, title, sectionType, methodType);
        generatedText = parseResponse(response);
        log.debug(generatedText);

        return AISuggestion.builder().generatedSuggestion(generatedText).build();
    }

    private String parseResponse(String rawResponse) {
        String beautifiedResponse;
        if (Objects.isNull(rawResponse) || rawResponse.isEmpty())
            throw new InternalServerErrorException("No response received from the AI.");

        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            if (root.has(Constant.CHOICES) && root.get(Constant.CHOICES).isArray()) {
                beautifiedResponse = root.get("choices").get(0).get("message").get("content").asText();
            } else {
                throw new InternalServerErrorException("Unexpected response format or empty array.");
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        log.debug(beautifiedResponse);
        return beautifiedResponse;
    }
}
