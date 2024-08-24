package com.ai.resume.builder.models;

import jakarta.persistence.*;
import lombok.*;

/**
 * The type User role.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private User user;
    @ManyToOne
    private Role role;
}