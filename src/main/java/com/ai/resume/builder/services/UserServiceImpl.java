package com.ai.resume.builder.services;

import com.ai.resume.builder.cache.Cache;
import com.ai.resume.builder.exceptions.BadRequestException;
import com.ai.resume.builder.models.Notification;
import com.ai.resume.builder.models.Role;
import com.ai.resume.builder.models.User;
import com.ai.resume.builder.models.UserRole;
import com.ai.resume.builder.repository.RoleRepository;
import com.ai.resume.builder.repository.UserRepository;
import com.ai.resume.builder.repository.UserRoleRepository;
import com.ai.resume.builder.utilities.Constant;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Cache cache;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User registerUser(User user) {
        if (user.isAuthTypeJwt())
            throw new BadRequestException("authTypeJwt field must be true for manual registration");

        User existingUser = cache.getUserByUsername(user.getUsername());

        if (!Objects.isNull(existingUser))
            throw new BadRequestException("User already exists.");

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        Set<UserRole> userRoles = DefaultValuesPopulator.populateDefaultUserRoles(user, roleRepository);
        for (UserRole ur : userRoles) roleRepository.save(ur.getRole());
        user.getRoles().addAll(userRoles);

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
    @CacheEvict(value = "userCache", allEntries = true)
    public void deleteUserByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setResumes(null);
        user.setRoles(null);
        user.setPaymentDetails(null);
        userRepository.delete(user);
    }

    @Override
    public void updateNotificationEnabled(Notification notification) {
        boolean isNotificationEnabled = notification.getIsNotificationEnabled();
        long userId = notification.getUserId();

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isNotificationEnabled() != isNotificationEnabled) {
            user.setNotificationEnabled(isNotificationEnabled);
            userRepository.save(user);
        }
    }

    @Override
    @CacheEvict(value = "userCache", allEntries = true)
    public void cancelPremiumMembership(long userId) {
        User user = cache.getUserById(userId);
        Role r = roleRepository.findByRoleName(Constant.PREMIUM_USER);

        user.getRoles().removeIf(userRole -> {
            if (userRole.getRole().getId() == r.getId()) {
                userRole.setUser(null);
                userRoleRepository.delete(userRole);
                return true;
            }
            return false;
        });
        userRepository.save(user);
    }
}
