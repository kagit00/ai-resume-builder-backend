package com.ai.resume.builder.utilities;

import com.ai.resume.builder.exceptions.BadRequestException;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.repository.ResumeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import java.util.NoSuchElementException;
import java.util.UUID;

public final class BasicUtility {

    private BasicUtility() {
        throw new UnsupportedOperationException("Not supported");
    }


    /**
     * The function takes an object and converts it into a JSON string using the Jackson ObjectMapper.
     *
     * @param o The parameter "o" is an object that you want to convert into a JSON string.
     * @return The method is returning a string representation of the given object.
     */
    public static String stringifyObject(Object o) {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Failed stringifying object");
        }
    }

    /**
     * Read specific property string.
     *
     * @param body the body
     * @param prop the prop
     * @return the string
     */
    public static String readSpecificProperty(String body, String prop) {
        try {
            return JsonPath.read(body, prop);
        } catch (Exception e) {
            return "";
        }
    }

    public static Resume getResumeById(UUID resumeId, ResumeRepository resumeRepository) {
        return resumeRepository.findById(resumeId)
                .orElseThrow(() -> new NoSuchElementException(Constant.RUSUME_NOT_FOUND + resumeId));
    }
}
