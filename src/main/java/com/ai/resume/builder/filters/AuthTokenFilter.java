package com.ai.resume.builder.filters;

import com.ai.resume.builder.security.JwtUtils;
import com.ai.resume.builder.services.UserDetailsServiceImpl;
import com.ai.resume.builder.utilities.ErrorUtility;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

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
            String requestTokenHeader = request.getHeader("Authorization");
            String username = null;
            String jwtToken = null;
            if (!StringUtils.isEmpty(requestTokenHeader) && requestTokenHeader.startsWith("Bearer")) {
                jwtToken = requestTokenHeader.substring(7);
                username = this.jwtUtils.getUsernameFromToken(jwtToken);
            }
            if (!Objects.isNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (Boolean.TRUE.equals(this.jwtUtils.validateToken(jwtToken, userDetails))) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException | ServletException |
                 IOException ex) {
            logger.info(ex.getMessage());
            response.setStatus(401);
            ErrorUtility.printError(ex.getMessage(), response);
        }
    }
}