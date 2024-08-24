package com.ai.resume.builder.cache;

import com.ai.resume.builder.models.User;
import com.ai.resume.builder.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class Cache {
    private final UserRepository userRepository;

    public Cache(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Gets user by username.
     *
     * @param username the username
     * @return the user by username
     */
    @Cacheable(value = "cache", key = "#username", unless = "#result == null")
    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }
}