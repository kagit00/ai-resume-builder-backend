package com.ai.resume.builder.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordDTO {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
    private long userId;
}
