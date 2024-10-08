package com.ai.resume.builder.services;

import com.ai.resume.builder.cache.Cache;
import com.ai.resume.builder.exceptions.BadRequestException;
import com.ai.resume.builder.models.User;
import com.ai.resume.builder.models.UserRole;
import com.ai.resume.builder.repository.RoleRepository;
import com.ai.resume.builder.repository.UserRepository;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Cache cache;
    private final RoleRepository roleRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    public UserServiceImpl(UserRepository userRepository, Cache cache, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.cache = cache;
        this.roleRepository = roleRepository;
    }

    @Override
    public User registerUser(User user) {
        User existingUser = cache.getUserByUsername(user.getUsername());
        if (!Objects.isNull(existingUser))
            throw new BadRequestException("User already exists.");
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        Set<UserRole> userRoles = DefaultValuesPopulator.populateDefaultUserRoles(user);
        for (UserRole ur : userRoles) roleRepository.save(ur.getRole());
        user.getRoles().addAll(userRoles);
        user.setJwtUser(true);
        user.setBio("");
        user.setTimestamp(DefaultValuesPopulator.getCurrentTimestamp());
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        logger.info("Fetching user by {}", username);
        return cache.getUserByUsername(username);
    }

    @Override
    public User updateUserByUsername(String username, User user) {
        User existingUser = cache.getUserByUsername(username);
        if (Objects.isNull(existingUser))
            throw new BadRequestException("User doesn't exist.");
        if (!user.getUsername().equals(existingUser.getUsername()))
            existingUser.setUsername(user.getUsername());
        if (!user.getName().equals(existingUser.getName()))
            existingUser.setName(user.getName());
        userRepository.save(existingUser);
        return existingUser;
    }

    @Override
    public void deleteUserByUsername(String username) {
        User existingUser = cache.getUserByUsername(username);
        if (Objects.isNull(existingUser))
            throw new BadRequestException("User doesn't exist.");
        userRepository.delete(existingUser);
    }
}
