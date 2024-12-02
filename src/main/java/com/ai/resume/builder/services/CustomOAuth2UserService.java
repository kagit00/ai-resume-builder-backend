package com.ai.resume.builder.services;

import com.ai.resume.builder.models.Role;
import com.ai.resume.builder.repository.RoleRepository;
import com.ai.resume.builder.repository.UserRepository;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import com.ai.resume.builder.models.User;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // Extract user details from OAuth2User
        String username = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Retrieve the OAuth2 token
        String token = userRequest.getAccessToken().getTokenValue();
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("accessToken", token);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = User.builder()
                    .bio("").authTypeJwt(false).name(name).password("").username(username).isNotificationEnabled(true)
                    .createdAt(DefaultValuesPopulator.getCurrentTimestamp()).updatedAt(DefaultValuesPopulator.getCurrentTimestamp())
                    .build();

            Role userRole = DefaultValuesPopulator.populateDefaultUserRoles(roleRepository, entityManager);
            user.getRoles().add(userRole);
            userRepository.save(user);
        }

        return new DefaultOAuth2User(oAuth2User.getAuthorities(), attributes, "name");
    }
}