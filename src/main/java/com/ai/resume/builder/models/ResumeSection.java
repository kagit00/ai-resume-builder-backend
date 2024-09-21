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
    @Pattern(regexp = "^[a-zA-Z0-9 .,\\-()]{1,100}$", message = "Invalid organization name")
    private String organization;
    @Column(nullable = false)
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid start date format, expected YYYY-MM-DD")
    private String startDate;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid start date format, expected YYYY-MM-DD")
    private String endDate;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Pattern(regexp = "^[a-zA-Z0-9 .,\\-()]{1,100}$", message = "Invalid location format")
    private String location;
}
