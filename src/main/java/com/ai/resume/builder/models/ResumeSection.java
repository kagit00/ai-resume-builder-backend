package com.ai.resume.builder.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "resume_section")
@Entity
public class ResumeSection {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    @JsonBackReference
    private Resume resume;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SectionType sectionType;
    @Column(nullable = false)
    private String title;
    private String organization;
    @Column(nullable = false)
    private String startDate;
    private String endDate;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String location;
}
