package com.ai.resume.builder.utilities;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import org.json.JSONObject;

public final class TextExtractionUtility {

    private TextExtractionUtility() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public static String extractTextFromJson(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            // Extract the text from the JSON response
            return jsonObject
                    .getJSONArray("ParsedResults")
                    .getJSONObject(0)
                    .getString("ParsedText");
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
