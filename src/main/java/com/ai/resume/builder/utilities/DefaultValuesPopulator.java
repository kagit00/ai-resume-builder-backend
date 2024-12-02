package com.ai.resume.builder.utilities;

import com.ai.resume.builder.models.Role;
import com.ai.resume.builder.models.User;
import com.ai.resume.builder.repository.RoleRepository;
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


    public static Role populateDefaultUserRoles(RoleRepository roleRepository) {
        Role role = roleRepository.findByName(Constant.FREE_USER);

        if (Objects.isNull(role)) {
            logger.info("{} not found as a role. Creating.", Constant.FREE_USER);
            role = Role.builder().name(Constant.FREE_USER).build();
            role = roleRepository.save(role);
        }

        return role;
    }

    public static Set<Role> populateDefaultPremiumUserRoles(RoleRepository roleRepository) {
        Role role = roleRepository.findByName(Constant.PREMIUM_USER);

        if (Objects.isNull(role)) {
            logger.info("{} not found as a role. Creating", Constant.PREMIUM_USER);
            role = Role.builder().name(Constant.PREMIUM_USER).build();
            role = roleRepository.save(role);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        return roles;
    }
}
