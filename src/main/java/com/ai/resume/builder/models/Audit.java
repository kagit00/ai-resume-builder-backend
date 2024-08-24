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
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(columnDefinition = "TEXT")
    private String uri;
    private String timestamp;
    private String methodName;
    @Column(columnDefinition = "TEXT")
    private String request;
    @Column(columnDefinition = "TEXT")
    private String response;
    private String status;
}