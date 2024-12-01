package com.ai.resume.builder.services;

import com.ai.resume.builder.dto.UserRequest;
import com.ai.resume.builder.dto.UserResponse;
import com.ai.resume.builder.models.Notification;
import com.ai.resume.builder.models.PasswordDTO;
import com.ai.resume.builder.models.User;

public interface UserService {

    UserResponse registerUser(UserRequest user);
    UserResponse getUserByUsername(String username);
    UserResponse updateUserByUsername(String username, UserRequest user);

    void deleteUserByUserId(long userId);

    void updateNotificationEnabled(Notification notification);
    void cancelPremiumMembership(long userId);
    void changePassword(PasswordDTO passwordDTO);
}
