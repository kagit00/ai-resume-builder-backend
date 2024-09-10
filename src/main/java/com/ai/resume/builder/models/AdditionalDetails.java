package com.ai.resume.builder.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
    @Column(nullable = false)
    private String githubLink;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String linkedInProfileLink;
    @OneToOne
    @JoinColumn(name = "resume_id")
    @JsonBackReference
    private Resume resume;
}
