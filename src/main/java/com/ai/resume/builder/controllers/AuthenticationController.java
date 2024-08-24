package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.JwtRequest;
import com.ai.resume.builder.models.JwtResponse;
import com.ai.resume.builder.models.User;
import com.ai.resume.builder.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * The type Authentication controller.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    /**
     * Instantiates a new Authentication controller.
     *
     * @param authenticationService the authentication service
     */
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Generate token response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> generateToken(@RequestBody JwtRequest request) {
        return new ResponseEntity<>(this.authenticationService.generateToken(request), HttpStatus.OK);
    }

    @GetMapping(value = "/current-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getPrincipal(Principal principal) {
        return new ResponseEntity<>(this.authenticationService.getPrincipal(principal.getName()), HttpStatus.OK);
    }
}
