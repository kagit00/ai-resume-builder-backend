package com.ai.resume.builder.utilities;

import com.ai.resume.builder.security.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.util.HashMap;
import java.util.Map;

public final class AuthUtility {
    private AuthUtility() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public static Map<String, String> retrieveTokenAndUsername(HttpServletRequest request, JwtUtils jwtUtils) {
        Map<String, String> creds = new HashMap<>();
        String jwtToken = null;
        String username = null;
        String requestTokenHeader = request.getHeader("Authorization");

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            username = jwtUtils.getUsernameFromToken(jwtToken);
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
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

    public static void removeCookie(HttpServletResponse response, String cookieName) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
