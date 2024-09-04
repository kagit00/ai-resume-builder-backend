package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.BadRequestException;
import com.ai.resume.builder.models.JwtRequest;
import com.ai.resume.builder.models.JwtResponse;
import com.ai.resume.builder.models.User;
import com.ai.resume.builder.security.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${ui.domain.uri}")
    private String uiDomainUri;
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

    public JwtResponse generateToken(JwtRequest jwtRequest) {
        this.authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token = this.jwtUtils.generateToken(userDetails);
        long tokenExpiry = this.jwtUtils.getExpirationDateFromToken(token).getTime();
        return new JwtResponse(token, tokenExpiry);
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
