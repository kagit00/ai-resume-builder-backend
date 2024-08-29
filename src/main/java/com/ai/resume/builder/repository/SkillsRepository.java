package com.ai.resume.builder.repository;

import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SkillsRepository extends JpaRepository<Skill, UUID> {
    List<Skill> findByResume(Resume resume);
}
