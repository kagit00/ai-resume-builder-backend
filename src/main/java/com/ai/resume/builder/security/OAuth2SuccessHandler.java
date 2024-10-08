package com.ai.resume.builder.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
        // Production environment: set cookie
        logger.info("jwt token for oauth2 flow: {}", accessToken);
        response.addHeader("Set-Cookie", "GOOGLE_OAUTH2_TOKEN=" + accessToken + "; Path=/; SameSite=Lax");
        long tokenExpiresAt = jwtUtils.getExpirationDateFromToken(accessToken).getTime();
        logger.debug("token expires at and now{} {}", tokenExpiresAt, System.currentTimeMillis());
        response.addHeader("Set-Cookie", "GOOGLE_OAUTH2_TOKEN_EXPIRATION=" + tokenExpiresAt + "; Path=/; SameSite=Lax");
        response.sendRedirect(uiDomainUri + "/user/dashboard");
    }

    private String generateTokenForUser(Authentication authentication) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        return jwtUtils.generateToken(((String) user.getAttributes().get("email")));
    }
}
