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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AISuggestionsServiceImplementation implements AISuggestionsService {

    @Value("${hugging-face.api.base-url}")
    private String huggingFaceBaseUrl;
    @Value("${hugging-face.api.token}")
    private String huggingFaceApiKey;
    private final AISuggestion aiSuggestion = new AISuggestion();
    private static final Logger log = LoggerFactory.getLogger(AISuggestionsServiceImplementation.class);
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public AISuggestion generateSuggestions(String title, String sectionType) {
        String generatedText = "";
        String response = Connector.postRequest(huggingFaceBaseUrl, huggingFaceApiKey, title, sectionType);
        if ("professional experience".equals(sectionType))
            generatedText = formatResponseAsBulletPoints(response);
        else generatedText = parseResponse(response);
        aiSuggestion.setGeneratedSuggestion(generatedText);
        return aiSuggestion;
    }

    private String formatResponseAsBulletPoints(String rawResponse) {
        try {
            // Parse the JSON array and extract the first element's "generated_text" field
            JsonNode rootNode = objectMapper.readTree(rawResponse);
            String generatedText = rootNode.get(0).get(Constant.GENERATED_TEXT).asText();

            // Split the text by newline characters and remove any empty lines
            String[] lines = generatedText.split("\\n+");

            // Filter out empty lines and trim whitespace
            List<String> formattedLines = Arrays.stream(lines).map(String::trim).filter(line -> !line.isEmpty()).toList();

            // Join the lines with bullet points
            String bulletInPoints = formattedLines.stream().map(line -> "• " + line).collect(Collectors.joining("\n"));

            log.debug("\n {}", bulletInPoints);
            return bulletInPoints;
        } catch (Exception e) {
            log.error("Error processing response", e);
            return "Error processing response";
        }
    }

    private String parseResponse(String rawResponse) {
        String beautifiedResponse = "";
        if (Objects.isNull(rawResponse) || rawResponse.isEmpty()) return "No response received from the API.";

        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            if (root.isArray() && !root.isEmpty()) {
                JsonNode firstObject = root.get(0);
                if (firstObject.has(Constant.GENERATED_TEXT)) beautifiedResponse = firstObject.get(Constant.GENERATED_TEXT).asText();
                 else return "No 'generated_text' found in the response.";
            } else return "Unexpected response format or empty array.";
        } catch (JsonProcessingException e) {
            return "Failed to parse the API response: " + e.getMessage();
        }
        return beautifiedResponse;
    }
}
