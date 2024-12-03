package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.JwtRequest;
import com.ai.resume.builder.models.JwtResponse;
import com.ai.resume.builder.models.User;
import com.ai.resume.builder.services.AuthenticationService;
import com.ai.resume.builder.utilities.AuthUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

/**
 * The type Authentication controller.
 */
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "Authentication API", description = "Operations related to user authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Transactional
    @PostMapping(value = "/log-in", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> logIn(@RequestBody JwtRequest jwtRequest) {
        JwtResponse jwtTokenResponse = authenticationService.generateToken(jwtRequest);
        return new ResponseEntity<>(jwtTokenResponse, HttpStatus.OK);
    }

    @Operation(summary = "Log Out of The System",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @PostMapping(value = "/log-out", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "userCache", allEntries = true)
    public ResponseEntity<Object> logOut(HttpServletResponse response) {
        AuthUtility.logOut(response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Fetch The Current User",
            description = "Requires either JWT or OAuth2 for authentication",
            security = {
                    @SecurityRequirement(name = "JWT"),
                    @SecurityRequirement(name = "OAuth2")
            })
    @Transactional
    @GetMapping(value = "/current-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getPrincipal(Principal principal) {
        return new ResponseEntity<>(authenticationService.getPrincipal(principal.getName()), HttpStatus.OK);
    }
}
