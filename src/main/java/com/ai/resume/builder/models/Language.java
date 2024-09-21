package com.ai.resume.builder.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.util.EnumSet;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "language")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    @JsonBackReference
    private Resume resume;

    @Column(nullable = false, length = 50)
    @Pattern(regexp = "^[a-zA-Z ]{1,50}$", message = "Invalid language name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "proficiency_level", nullable = false)
    private ProficiencyLevel proficiencyLevel;

    @PrePersist
    @PreUpdate
    private void validateProficiencyLevel() {
        if (proficiencyLevel == null) {
            throw new IllegalArgumentException("Proficiency level cannot be null");
        }

        if (!EnumSet.of(ProficiencyLevel.NAIVE, ProficiencyLevel.NATIVE, ProficiencyLevel.FLUENT, ProficiencyLevel.EXPERT)
                .contains(proficiencyLevel)) {
            throw new IllegalArgumentException("Invalid proficiency level: " + proficiencyLevel);
        }
    }
}
