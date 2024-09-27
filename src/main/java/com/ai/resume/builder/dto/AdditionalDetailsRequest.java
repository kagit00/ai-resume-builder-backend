package com.ai.resume.builder.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalDetailsRequest {
    @Pattern(regexp = "^https:\\/\\/github\\.com\\/[a-zA-Z0-9-]+$", message = "Invalid GitHub URL")
    private String githubLink;
    
    @Pattern(regexp = "^\\+?[0-9. ()-]{10,13}$", message = "Invalid phone number")
    private String phoneNumber;
    
    @Pattern(regexp = "^https:\\/\\/(www\\.)?linkedin\\.com\\/in\\/[a-zA-Z0-9-]+\\/?$", message = "Invalid LinkedIn URL")
    private String linkedInProfileLink;
}
