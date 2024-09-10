package com.ai.resume.builder.repository;

import com.ai.resume.builder.models.Role;
import com.ai.resume.builder.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByRole(Role role);
}
