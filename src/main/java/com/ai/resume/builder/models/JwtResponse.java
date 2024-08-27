package com.ai.resume.builder.models;

import lombok.*;

/**
 * The type Jwt response.
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class JwtResponse {
    private String token;
    private long expiry;
}