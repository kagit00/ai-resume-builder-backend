package com.ai.resume.builder.utilities;

import static org.junit.jupiter.api.Assertions.*;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class TextExtractionUtilityTest {

    @Test
    void extractTextFromJson_validJson_returnsExtractedText() {
        String validJson = new JSONObject()
                .put("ParsedResults", new org.json.JSONArray()
                        .put(new JSONObject().put("ParsedText", "Extracted Text")))
                .toString();

        String result = TextExtractionUtility.extractTextFromJson(validJson);

        assertEquals("Extracted Text", result);
    }

    @Test
    void extractTextFromJson_jsonWithoutParsedResults_throwsException() {
        String invalidJson = new JSONObject().put("InvalidKey", "Some Value").toString();

        Exception exception = assertThrows(InternalServerErrorException.class, () -> {
            TextExtractionUtility.extractTextFromJson(invalidJson);
        });
        assertTrue(exception.getMessage().contains("ParsedResults array is missing or empty"));
    }

    @Test
    void extractTextFromJson_emptyParsedResultsArray_throwsException() {
        String jsonWithEmptyArray = new JSONObject().put("ParsedResults", new org.json.JSONArray()).toString();

        Exception exception = assertThrows(InternalServerErrorException.class, () -> {
            TextExtractionUtility.extractTextFromJson(jsonWithEmptyArray);
        });
        assertTrue(exception.getMessage().contains("ParsedResults array is missing or empty"));
    }

    @Test
    void extractTextFromJson_nullJson_throwsException() {
        Exception exception = assertThrows(InternalServerErrorException.class, () -> {
            TextExtractionUtility.extractTextFromJson(null);
        });
        assertTrue(exception.getMessage().contains("JSON response is null or empty"));
    }

    @Test
    void extractTextFromJson_malformedJson_throwsException() {
        String malformedJson = "{ParsedResults: [ParsedText: 'Missing quotes";

        Exception exception = assertThrows(InternalServerErrorException.class, () -> {
            TextExtractionUtility.extractTextFromJson(malformedJson);
        });

        assertTrue(exception.getMessage().contains("Error processing JSON response"));
    }
}
