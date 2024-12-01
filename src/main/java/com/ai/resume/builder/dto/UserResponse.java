package com.ai.resume.builder.dto;

import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.UserRole;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {
    private Long id;
    private String username;
    private String name;
    @Builder.Default
    private List<Resume> resumes = new ArrayList<>();
    @Builder.Default
    private Set<UserRole> roles = new HashSet<>();
    private String password;
    private String bio;
}
