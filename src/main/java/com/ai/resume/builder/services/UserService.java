package com.ai.resume.builder.services;

import com.ai.resume.builder.models.Notification;
import com.ai.resume.builder.models.User;

public interface UserService {
    /**
     * Register user user.
     *
     * @param user the user
     * @return the user
     */
    User registerUser(User user);

    /**
     * Gets user by username.
     *
     * @param username the username
     * @return the user by username
     */
    User getUserByUsername(String username);

    /**
     * Update user by username user.
     *
     * @param username the username
     * @param user     the user
     * @return the user
     */
    User updateUserByUsername(String username, User user);

    void deleteUserByUserId(long userId);

    void updateNotificationEnabled(Notification notification);
    void cancelPremiumMembership(long userId);
}
