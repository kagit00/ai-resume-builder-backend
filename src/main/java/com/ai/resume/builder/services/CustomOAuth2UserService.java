package com.ai.resume.builder.services;

import com.ai.resume.builder.models.UserRole;
import com.ai.resume.builder.repository.RoleRepository;
import com.ai.resume.builder.repository.UserRepository;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import com.ai.resume.builder.models.User;

@Service
@AllArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // Extract user details from OAuth2User
        String username = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Retrieve the OAuth2 token
        String token = userRequest.getAccessToken().getTokenValue();
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("accessToken", token);

        // Save or update user information in the database
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            user = new User();
            user.setUsername(username);
            user.setName(name);
            user.setOauthUser(true);
            Set<UserRole> userRoles = DefaultValuesPopulator.populateDefaultUserRoles(user);
            for (UserRole ur : userRoles)
                roleRepository.save(ur.getRole());
            user.getRoles().addAll(userRoles);
            user.setPassword("");
            user.setBio("");
            user.setTimestamp(DefaultValuesPopulator.getCurrentTimestamp());
            userRepository.save(user);
        }

        return new DefaultOAuth2User(oAuth2User.getAuthorities(), attributes, "name");
    }
}