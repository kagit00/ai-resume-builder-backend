package com.ai.resume.builder.controllers;

import com.ai.resume.builder.dto.UserRequest;
import com.ai.resume.builder.dto.UserResponse;
import com.ai.resume.builder.models.NoContent;
import com.ai.resume.builder.models.Notification;
import com.ai.resume.builder.models.PasswordDTO;
import com.ai.resume.builder.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Tag(name = "User API", description = "Operations related to User")
public class UserController {
    private final UserService userService;

    /**
     * Register user response entity.
     *
     * @param user the user
     * @return the response entity
     */
    @Operation(summary = "Register User")
    @Transactional
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> registerUser(@Validated @RequestBody UserRequest user) {
        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.OK);
    }

    /**
     * Gets user by username.
     *
     * @param username the username
     * @return the user by username
     */
    @Operation(summary = "Get User By Username",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable("username") String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    /**
     * Update user by username response entity.
     *
     * @param username the username
     * @param user     the user
     * @return the response entity
     */
    @Operation(summary = "Update User By Username",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @PutMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> updateUserByUsername(@PathVariable("username") String username, @Valid @RequestBody UserRequest user) {
        return new ResponseEntity<>(userService.updateUserByUsername(username, user), HttpStatus.OK);
    }

    @Operation(summary = "Delete User By Id",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteUserById(@PathVariable("userId") long userId) {
        userService.deleteUserByUserId(userId);
        return new ResponseEntity<>(new NoContent(HttpStatus.OK, "User successfully deleted."), HttpStatus.OK);
    }

    @Operation(summary = "Update Notification Enabled",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @PutMapping(value = "/notification", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateNotificationEnabled(@RequestBody Notification notification) {
        userService.updateNotificationEnabled(notification);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Cancel Premium Membership",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @PutMapping(value = "/cancel-membership/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> cancelPremiumMembership(@PathVariable("userId") long userId) {
        userService.cancelPremiumMembership(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Operation(summary = "Update Password",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @PutMapping(value = "/change-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updatePassword(@RequestBody PasswordDTO passwordDTO) {
        userService.changePassword(passwordDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
