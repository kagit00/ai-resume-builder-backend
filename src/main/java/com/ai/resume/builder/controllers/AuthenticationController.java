package com.ai.resume.builder.controllers;

import com.ai.resume.builder.models.JwtRequest;
import com.ai.resume.builder.models.JwtResponse;
import com.ai.resume.builder.models.User;
import com.ai.resume.builder.services.AuthenticationService;
import com.ai.resume.builder.utilities.AuthUtility;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

/**
 * The type Authentication controller.
 */
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/log-in", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> logIn(@RequestBody JwtRequest jwtRequest) {
        JwtResponse jwtTokenResponse = authenticationService.generateToken(jwtRequest);
        return new ResponseEntity<>(jwtTokenResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/log-out", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> logOut(HttpServletResponse response) {
        AuthUtility.logOut(response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/current-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getPrincipal(Principal principal) {
        return new ResponseEntity<>(this.authenticationService.getPrincipal(principal.getName()), HttpStatus.OK);
    }
}
