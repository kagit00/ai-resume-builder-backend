package com.ai.resume.builder.services;

import com.ai.resume.builder.models.Skill;

import java.util.List;
import java.util.UUID;

public interface SkillsService {
    void saveSkills(List<Skill> skills, UUID resumeId);
    List<Skill> getSkills(UUID resumeId);
}
