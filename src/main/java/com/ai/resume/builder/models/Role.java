package com.ai.resume.builder.models;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * The type Role.
 */
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "role_name", nullable = false, unique = true)
    private String name;
}