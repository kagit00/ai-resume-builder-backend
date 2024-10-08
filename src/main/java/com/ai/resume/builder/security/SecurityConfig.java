package com.ai.resume.builder.security;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.filters.AuthTokenFilter;
import com.ai.resume.builder.filters.JwtAuthenticationEntryPoint;
import com.ai.resume.builder.repository.RoleRepository;
import com.ai.resume.builder.repository.UserRepository;
import com.ai.resume.builder.services.CustomOAuth2UserService;
import com.ai.resume.builder.utilities.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

/**
 * The type Security config.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Value("${ui.domain.uri}")
    private String uiDomainUri;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;
    private final OAuth2SuccessHandler successHandler;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /**
     * Instantiates a new Security config.
     *
     * @param unauthorizedHandler the unauthorized handler
     * @param authTokenFilter     the auth token filter
     */
    public SecurityConfig(
            JwtAuthenticationEntryPoint unauthorizedHandler,
            AuthTokenFilter authTokenFilter,
            OAuth2SuccessHandler successHandler,
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.authTokenFilter = authTokenFilter;
        this.successHandler = successHandler;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Password encoder password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     *
     * @param authenticationConfiguration the authentication configuration
     * @return the authentication manager
     * @throws Exception the exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return new CustomOAuth2UserService(userRepository, roleRepository);
    }


    /**
     * Filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        try {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .cors(cors -> cors
                            .configurationSource(request -> {
                                CorsConfiguration corsConfiguration = new CorsConfiguration();
                                corsConfiguration.setAllowedOrigins(List.of(uiDomainUri));
                                corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                                corsConfiguration.addAllowedHeader("*");
                                corsConfiguration.setAllowCredentials(true);
                                return corsConfiguration;
                            }))
                    .authorizeHttpRequests(auth ->
                            auth
                                    .requestMatchers("/oauth2/", "/login/**", "/error").permitAll()
                                    .requestMatchers("/users", "/auth/token").permitAll()
                                    .requestMatchers(
                                            "/users/**"
                                    )
                                    .hasAnyAuthority(Constant.FREE_USER, Constant.PREMIUM_USER)
                                    .anyRequest().authenticated()
                    ).exceptionHandling(ex ->
                            ex.authenticationEntryPoint(unauthorizedHandler)
                    )
                    .sessionManagement(session ->
                            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                    .oauth2Login(oauth2 ->
                            oauth2.userInfoEndpoint(userInfo -> userInfo
                                    .userService(oauth2UserService())
                            ).successHandler(successHandler)
            );

            return http.build();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
