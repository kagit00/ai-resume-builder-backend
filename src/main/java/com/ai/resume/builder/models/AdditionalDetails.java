package com.ai.resume.builder.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AdditionalDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Pattern(regexp = "^https:\\/\\/github\\.com\\/[a-zA-Z0-9-]+$", message = "Invalid GitHub URL")
    @Column(nullable = false)
    private String githubLink;
    @Pattern(regexp = "^\\+?[0-9. ()-]{10,13}$", message = "Invalid phone number")
    @Column(nullable = false)
    private String phoneNumber;
    @Pattern(regexp = "^https:\\/\\/(www\\.)?linkedin\\.com\\/in\\/[a-zA-Z0-9-]+\\/?$", message = "Invalid LinkedIn URL")
    @Column(nullable = false)
    private String linkedInProfileLink;
    @OneToOne
    @JoinColumn(name = "resume_id")
    @JsonBackReference
    private Resume resume;
}
