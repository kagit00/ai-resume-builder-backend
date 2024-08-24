package com.ai.resume.builder.models;

import lombok.*;

/**
 * The type Jwt request.
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest {
    private String username;
    private String password;
}
