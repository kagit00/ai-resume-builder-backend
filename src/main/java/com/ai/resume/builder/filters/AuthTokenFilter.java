package com.ai.resume.builder.filters;

import com.ai.resume.builder.security.JwtUtils;
import com.ai.resume.builder.services.UserDetailsServiceImpl;
import com.ai.resume.builder.utilities.ErrorUtility;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

/**
 * The type Auth token filter.
 */
@Component
@Order(1)
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Instantiates a new Auth token filter.
     *
     * @param jwtUtils           the jwt utils
     * @param userDetailsService the user details service
     */
    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        try {
            Map<String, String> creds = retrieveTokenAndUsername(request, jwtUtils);
            String username = creds.get("username");
            String jwtToken = creds.get("jwtToken");

            if (!Objects.isNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (Boolean.TRUE.equals(this.jwtUtils.validateToken(jwtToken, userDetails))) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException | ServletException | IOException ex) {
            response.setStatus(500);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ErrorUtility.printError(ex.getMessage(), response);
        }
    }

    private Map<String, String> retrieveTokenAndUsername(HttpServletRequest request, JwtUtils jwtUtils) {
        Map<String, String> creds = new HashMap<>();
        String jwtToken = null;
        String username = null;
        String requestTokenHeader = request.getHeader("Authorization");

        if (!StringUtils.isEmpty(requestTokenHeader) && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            username = jwtUtils.getUsernameFromToken(jwtToken);
        } else {
            Cookie[] cookies = request.getCookies();
            if (!Objects.isNull(cookies)) {
                for (Cookie cookie : cookies) {
                    if ("GOOGLE_OAUTH2_TOKEN".equals(cookie.getName())) {
                        jwtToken = cookie.getValue();
                        username = jwtUtils.getUsernameFromToken(jwtToken);
                    }
                }
            }
        }
        creds.put("username", username);
        creds.put("jwtToken", jwtToken);
        return creds;
    }

}