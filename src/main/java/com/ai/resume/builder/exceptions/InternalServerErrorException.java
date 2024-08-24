package com.ai.resume.builder.exceptions;

public class InternalServerErrorException extends RuntimeException {
    /**
     * Instantiates a new Bad request exception.
     *
     * @param m the m
     */
    public InternalServerErrorException(String m) {
        super(m);
    }
}