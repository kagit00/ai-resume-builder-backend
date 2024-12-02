package com.ai.resume.builder.utilities;

import com.ai.resume.builder.models.Role;
import com.ai.resume.builder.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import java.util.Objects;
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


    @Transactional
    public static Role populateDefaultUserRoles(RoleRepository roleRepository, EntityManager entityManager) {
        Role role = roleRepository.findByName(Constant.FREE_USER);

        if (Objects.isNull(role)) {
            logger.info("{} not found as a role. Creating.", Constant.FREE_USER);
            role = Role.builder().name(Constant.FREE_USER).build();
            role = roleRepository.save(role);
        }

        return entityManager.merge(role);
    }

    @Transactional
    public static Role populateDefaultPremiumUserRoles(RoleRepository roleRepository, EntityManager entityManager) {
        Role role = roleRepository.findByName(Constant.PREMIUM_USER);

        if (Objects.isNull(role)) {
            logger.info("{} not found as a role. Creating", Constant.PREMIUM_USER);
            role = Role.builder().name(Constant.PREMIUM_USER).build();
            role = roleRepository.save(role);
        }

        return entityManager.merge(role);
    }
}
