package com.ai.resume.builder.services;

import com.ai.resume.builder.cache.Cache;
import com.ai.resume.builder.dto.UserRequest;
import com.ai.resume.builder.dto.UserResponse;
import com.ai.resume.builder.exceptions.BadRequestException;
import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.*;
import com.ai.resume.builder.repository.RoleRepository;
import com.ai.resume.builder.repository.UserRepository;
import com.ai.resume.builder.utilities.Constant;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import com.ai.resume.builder.utilities.ResponseMakerUtility;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Cache cache;
    private final RoleRepository roleRepository;


    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        if (!userRequest.isAuthTypeJwt()) {
            throw new BadRequestException("authTypeJwt field must be true for manual registration");
        }
        User existingUser = cache.getUserByUsername(userRequest.getUsername());

        if (!Objects.isNull(existingUser)) {
            throw new BadRequestException("User already exists.");
        }
        User user = User.builder()
                .bio(userRequest.getBio()).authTypeJwt(userRequest.isAuthTypeJwt()).name(userRequest.getName()).isNotificationEnabled(true)
                .password(new BCryptPasswordEncoder().encode(userRequest.getPassword())).username(userRequest.getUsername())
                .createdAt(DefaultValuesPopulator.getCurrentTimestamp()).updatedAt(DefaultValuesPopulator.getCurrentTimestamp())
                .build();

        Set<UserRole> userRoles = DefaultValuesPopulator.populateDefaultUserRoles(user, roleRepository);
        for (UserRole ur : userRoles)
            roleRepository.save(ur.getRole());

        user.getRoles().addAll(userRoles);
        userRepository.save(user);

        return ResponseMakerUtility.getUserResponse(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        User user = cache.getUserByUsername(username);
        return ResponseMakerUtility.getUserResponse(user);
    }

    @Override
    public UserResponse updateUserByUsername(String username, UserRequest userRequest) {
        User existingUser = cache.getUserByUsername(username);

        if (Objects.isNull(existingUser))
            throw new BadRequestException("User doesn't exist.");

        if (!userRequest.getUsername().equals(existingUser.getUsername()))
            existingUser.setUsername(userRequest.getUsername());

        if (!userRequest.getName().equals(existingUser.getName()))
            existingUser.setName(userRequest.getName());

        userRepository.save(existingUser);
        return ResponseMakerUtility.getUserResponse(existingUser);
    }

    @Override
    @CacheEvict(value = "userCache", allEntries = true)
    public void deleteUserByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        user.getResumes().clear();
        user.getRoles().clear();

        userRepository.delete(user);
    }

    @Override
    @CacheEvict(value = "userCache", allEntries = true)
    public void updateNotificationEnabled(Notification notification) {
        boolean isNotificationEnabled = notification.getIsNotificationEnabled();
        long userId = notification.getUserId();

        User user = userRepository.findById(userId).orElseThrow(() -> new InternalServerErrorException("User not found"));

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
                return true;
            }
            return false;
        });
        userRepository.save(user);
    }

    @Override
    @CacheEvict(value = "userCache", allEntries = true)
    public void changePassword(PasswordDTO passwordDTO) {
        User user = cache.getUserById(passwordDTO.getUserId());
        String existingPasswordHash = user.getPassword();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(passwordDTO.getCurrentPassword(), existingPasswordHash)) {
            throw new BadRequestException("Current password is wrong.");
        }

        if (passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new BadRequestException("New Password Doesn't Match Confirmed Password.");
        }
    }
}
