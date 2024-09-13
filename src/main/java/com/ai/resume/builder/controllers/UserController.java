package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.NoContent;
import com.ai.resume.builder.models.Notification;
import com.ai.resume.builder.models.User;
import com.ai.resume.builder.services.UserServiceImpl;
import com.ai.resume.builder.validation.ValidPassword;
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
public class UserController {
    private final UserServiceImpl userService;

    /**
     * Register user response entity.
     *
     * @param user the user
     * @return the response entity
     */
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> registerUser(@Validated @ValidPassword @RequestBody User user) {
        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.OK);
    }

    /**
     * Gets user by username.
     *
     * @param username the username
     * @return the user by username
     */
    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    /**
     * Update user by username response entity.
     *
     * @param username the username
     * @param user     the user
     * @return the response entity
     */
    @Transactional
    @PutMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUserByUsername(@PathVariable("username") String username, @Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.updateUserByUsername(username, user), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Object> deleteUserById(@PathVariable("userId") long userId) {
        userService.deleteUserByUserId(userId);
        return new ResponseEntity<>(new NoContent(HttpStatus.OK, "User successfully deleted."), HttpStatus.OK);
    }

    @Transactional
    @PutMapping(value = "/notification", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateNotificationEnabled(@RequestBody Notification notification) {
        userService.updateNotificationEnabled(notification);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PutMapping(value = "/cancel-membership/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cancelPremiumMembership(@PathVariable("userId") long userId) {
        userService.cancelPremiumMembership(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
