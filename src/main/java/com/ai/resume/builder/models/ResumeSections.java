package com.ai.resume.builder.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "resume_sections")
@Entity
public class ResumeSections {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SectionType sectionType;
    @Column(nullable = false)
    private String title;
    private String organization;
    @Column(nullable = false)
    private String startDate;
    @Column(nullable = false)
    private String endDate;
    @Lob
    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;
}
