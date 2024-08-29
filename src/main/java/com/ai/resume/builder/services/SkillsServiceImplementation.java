package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.Skill;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.repository.SkillsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SkillsServiceImplementation implements SkillsService {
    private final SkillsRepository skillsRepository;
    private final ResumeRepository resumeRepository;

    @Override
    public void saveSkills(List<Skill> skills, UUID resumeId) {
        if (Objects.isNull(skills) || Objects.isNull(resumeId)) {
            throw new InternalServerErrorException("skills or resume id is null");
        }

        Resume resume = resumeRepository.findById(resumeId).orElseThrow();

        skills.forEach(skill -> {
            skill.setResume(resume);
            skillsRepository.save(skill);
        });
    }

    @Override
    public List<Skill> getSkills(UUID resumeId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow();
        return skillsRepository.findByResume(resume);
    }
}
