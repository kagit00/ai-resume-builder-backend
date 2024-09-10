package com.ai.resume.builder.utilities;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.Role;
import com.ai.resume.builder.models.User;
import com.ai.resume.builder.models.UserRole;
import com.ai.resume.builder.repository.RoleRepository;
import com.ai.resume.builder.services.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class DefaultValuesPopulator {

    private DefaultValuesPopulator() {
        throw new UnsupportedOperationException("Operation not supported");
    }
    private static final Logger logger = LoggerFactory.getLogger(DefaultValuesPopulator.class);


    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public static String getCurrentTimestamp() {
        return OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC).toString();
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public static String getUid() {
        return UUID.randomUUID().toString();
    }


    public static Set<UserRole> populateDefaultUserRoles(User user, RoleRepository roleRepository) {
        Role r = roleRepository.findByRoleName(Constant.FREE_USER);
        if (Objects.isNull(r)) {
            logger.info("{} not found as a role. Creating ", Constant.FREE_USER);
            r = new Role();
            r.setId(1L);
            r.setRoleName(Constant.FREE_USER);
            roleRepository.save(r);
        }

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(r);

        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(userRole);
        return userRoles;
    }

    public static Set<UserRole> populateDefaultPremiumUserRoles(User user, RoleRepository roleRepository) {
        Role r = roleRepository.findByRoleName(Constant.PREMIUM_USER);
        if (Objects.isNull(r)) {
            logger.info("{} not found as a role. Creating the same.", Constant.PREMIUM_USER);
            r = new Role();
            r.setId(2L);
            r.setRoleName(Constant.PREMIUM_USER);
            roleRepository.save(r);
        }

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(r);

        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(userRole);
        return userRoles;
    }
}
