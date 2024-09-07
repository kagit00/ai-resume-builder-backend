package com.ai.resume.builder.cache;

import com.ai.resume.builder.models.Language;
import com.ai.resume.builder.models.Resume;
import com.ai.resume.builder.models.ResumeSection;
import com.ai.resume.builder.models.User;
import com.ai.resume.builder.repository.LanguageRepository;
import com.ai.resume.builder.repository.ResumeRepository;
import com.ai.resume.builder.repository.ResumeSectionsRepository;
import com.ai.resume.builder.repository.UserRepository;
import com.ai.resume.builder.utilities.Constant;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@AllArgsConstructor
public class Cache {
    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;
    private final ResumeRepository resumeRepository;
    private final ResumeSectionsRepository resumeSectionsRepository;

    /**
     * Gets user by username.
     *
     * @param username the username
     * @return the user by username
     */
    @Cacheable(value = "userCache", key = "#username", unless = "#result == null")
    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Cacheable(value = "resumeCache", key = "#resumeId", unless = "#result == null")
    public Resume getResumeById(UUID resumeId) {
        return resumeRepository.findById(resumeId)
                .orElseThrow(() -> new NoSuchElementException(Constant.RUSUME_NOT_FOUND + resumeId));
    }

    @Cacheable(value = "languageCache", key = "#languageId")
    public Language getLanguageById(UUID languageId) {
        return languageRepository.findById(languageId).orElseThrow(
                () -> new NoSuchElementException("Language not found with id: " + languageId)
        );
    }

    @Cacheable(value = "resumeSectionCache", key = "#resumeSectionId")
    public ResumeSection getResumeSectionById(UUID resumeSectionId) {
        return resumeSectionsRepository.findById(resumeSectionId).orElseThrow(
                () -> new NoSuchElementException("Resume Section not found with id: " + resumeSectionId)
        );
    }

    @Cacheable(value = "userCache", key = "#userId", unless = "#result == null")
    public User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found with id" ));
    }
}