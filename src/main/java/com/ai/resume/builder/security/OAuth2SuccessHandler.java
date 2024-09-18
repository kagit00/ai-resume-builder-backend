package com.ai.resume.builder.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.Duration;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Value("${ui.domain.uri}")
    private String uiDomainUri;
    private final JwtUtils jwtUtils;
    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    public OAuth2SuccessHandler(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String accessToken = generateTokenForUser(authentication);
        long tokenExpiresAt = jwtUtils.getExpirationDateFromToken(accessToken).getTime();

        ResponseCookie cookie1 = ResponseCookie.from("GOOGLE_OAUTH2_TOKEN", accessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        ResponseCookie cookie2 = ResponseCookie.from("OAUTH2_TOKEN_EXPIRY", String.valueOf(tokenExpiresAt))
                .secure(true)
                .sameSite("None")
                .path("/")
                .domain(uiDomainUri)
                .maxAge(Duration.ofHours(1))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie1.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie2.toString());

        logger.debug("jwt token for oauth2 flow: {}", accessToken);
        logger.debug("token expires at and now {} {}", tokenExpiresAt, System.currentTimeMillis());

        if (response.getStatus() == 200)
            response.sendRedirect(uiDomainUri + "/user/dashboard");
    }

    private String generateTokenForUser(Authentication authentication) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        return jwtUtils.generateToken(((String) user.getAttributes().get("email")));
    }
}
