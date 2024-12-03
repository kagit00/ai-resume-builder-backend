package com.ai.resume.builder.utilities;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import org.json.JSONException;
import org.json.JSONObject;

public final class TextExtractionUtility {

    private TextExtractionUtility() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public static String extractTextFromJson(String jsonResponse) {
        if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
            throw new InternalServerErrorException("JSON response is null or empty.");
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);

            if (!jsonObject.has("ParsedResults") || jsonObject.getJSONArray("ParsedResults").isEmpty()) {
                throw new InternalServerErrorException("ParsedResults array is missing or empty.");
            }

            return jsonObject
                    .getJSONArray("ParsedResults")
                    .getJSONObject(0)
                    .getString("ParsedText");
        } catch (JSONException e) {
            throw new InternalServerErrorException("Error processing JSON response: Invalid JSON structure");
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
