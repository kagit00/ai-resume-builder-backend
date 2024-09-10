package com.ai.resume.builder.models;

import com.ai.resume.builder.services.UserServiceImpl;
import com.ai.resume.builder.utilities.Constant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    private String name;
    @Column(nullable = false)
    private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Resume> resumes = new ArrayList<>();
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,  mappedBy = "user")
    private Set<UserRole> roles = new HashSet<>();
    private boolean isJwtUser;
    private boolean isOauthUser;
    private String bio;
    @Column(nullable = false)
    private String timestamp;
    @Column(nullable = false)
    private boolean isNotificationEnabled;

    private static final Logger log = LoggerFactory.getLogger(User.class);

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Authority> authorities = new HashSet<>();
        log.debug("User {} has roles: {}", this.username, this.roles.size());
        this.roles.forEach(userRole -> {
            Role role = userRole.getRole(); // Fetch the role
            if (role != null) {
                log.debug("Adding role authority: {}", role.getRoleName());
                authorities.add(new Authority(role.getRoleName()));
            } else {
                log.warn("Null role found for user {}", this.username);
            }
        });

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(userRole -> userRole.getRole().getRoleName().equalsIgnoreCase(roleName));
    }

    public boolean isPremiumUser() {
        return hasRole(Constant.PREMIUM_USER);
    }

    public boolean isFreeUser() {
        return hasRole(Constant.FREE_USER);
    }
}
