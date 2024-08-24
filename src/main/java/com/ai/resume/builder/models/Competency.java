package com.ai.resume.builder.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Competency {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompetencyType competencyType;
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private ProficiencyLevel proficiencyLevel;
}
