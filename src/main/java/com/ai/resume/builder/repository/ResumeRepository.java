package com.ai.resume.builder.repository;

import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ResumeRepository extends JpaRepository<Resume, UUID> {
    List<Resume> findByUser(User user);
}
