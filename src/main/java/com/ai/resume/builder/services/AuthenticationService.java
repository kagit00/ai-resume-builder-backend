package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.BadRequestException;
import com.ai.resume.builder.models.JwtRequest;
import com.ai.resume.builder.models.JwtResponse;
import com.ai.resume.builder.models.User;
import com.ai.resume.builder.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * The type Authentication service.
 */
@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    /**
     * Instantiates a new Authentication service.
     *
     * @param jwtUtils              the jwt utils
     * @param userDetailsService    the user details service
     * @param authenticationManager the authentication manager
     */
    public AuthenticationService(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Generate token jwt response.
     *
     * @param request the request
     * @return the jwt response
     */
    public JwtResponse generateToken(JwtRequest request) {
        this.authenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.jwtUtils.generateToken(userDetails);
        return new JwtResponse(token);
    }

    private void authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authenticationManager.authenticate(authentication);
        } catch (DisabledException e) {
            throw new BadRequestException("User Disabled" + e.getMessage());
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Bad Credentials" + e.getMessage());
        }
    }

    public User getPrincipal(String principal) {
        return (User) this.userDetailsService.loadUserByUsername(principal);
    }
}
