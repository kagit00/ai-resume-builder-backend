package com.ai.resume.builder.utilities;

import com.ai.resume.builder.models.Role;
import com.ai.resume.builder.models.User;
import com.ai.resume.builder.models.UserRole;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class DefaultValuesPopulator {

    private DefaultValuesPopulator() {
        throw new UnsupportedOperationException("Operation not supported");
    }

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


    public static Set<UserRole> populateDefaultUserRoles(User user) {
        Role role = new Role();
        role.setRoleName(Constant.FREE_USER);
        role.setId(2L);

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(userRole);
        return userRoles;
    }
}
