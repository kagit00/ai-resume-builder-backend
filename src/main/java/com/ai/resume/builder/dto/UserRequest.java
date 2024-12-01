package com.ai.resume.builder.dto;

import com.ai.resume.builder.validation.ValidName;
import com.ai.resume.builder.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequest {
    @NotBlank(message = "Username (email) is required and cannot be blank.")
    @Email(message = "Username must be a valid email address.")
    private String username;

    @ValidName
    private String name;

    @ValidPassword
    private String password;

    @NotNull
    private boolean authTypeJwt;
    private String bio;
}
