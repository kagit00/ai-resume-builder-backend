package com.ai.resume.builder.cache;

import com.ai.resume.builder.models.*;
import com.ai.resume.builder.repository.LanguageRepository;
import com.ai.resume.builder.repository.ResumeSectionsRepository;
import com.ai.resume.builder.repository.RoleRepository;
import com.ai.resume.builder.repository.UserRepository;
import com.ai.resume.builder.utilities.Constant;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.*;

@SpringBootTest
class CacheTest {

    @Autowired
    private Cache cacheService;

    @MockBean
    private ResumeSectionsRepository resumeSectionsRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private LanguageRepository languageRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    void testGetUserByUsername_ShouldCacheResult() {
        String username = "john_doe";
        User mockUser = User.builder().username("john123").name("John Doe").build();
        when(userRepository.findByUsername(username)).thenReturn(mockUser);

        // First call: should query the repository
        User user1 = cacheService.getUserByUsername(username);
        Assertions.assertEquals("john123", user1.getUsername());

        // Second call: should fetch from cache
        User user2 = cacheService.getUserByUsername(username);
        Assertions.assertSame(user1, user2);

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGetLanguageById_ShouldCacheResult() {
        UUID languageId = UUID.fromString(DefaultValuesPopulator.getUid());
        Language mockLanguage = Language.builder().name("English").proficiencyLevel(ProficiencyLevel.NAIVE).build();
        when(languageRepository.findById(languageId)).thenReturn(Optional.ofNullable(mockLanguage));

        // First call: should query the repository
        Language language1 = cacheService.getLanguageById(languageId);
        Assertions.assertEquals("English", language1.getName());

        // Second call: should fetch from cache
        Language language2 = cacheService.getLanguageById(languageId);
        Assertions.assertSame(language1, language2);

        verify(languageRepository, times(1)).findById(languageId);
    }

    @Test
    void testGetResumeSectionById_ShouldCacheResult() {
        UUID resumeSectionId = UUID.fromString(DefaultValuesPopulator.getUid());
        ResumeSection mockSection = ResumeSection.builder().sectionType(SectionType.EDUCATION).title("B.Tech").build();
        when(resumeSectionsRepository.findById(resumeSectionId)).thenReturn(Optional.of(mockSection));

        // First call: should query the repository
        ResumeSection section1 = cacheService.getResumeSectionById(resumeSectionId);
        Assertions.assertEquals("B.Tech", section1.getTitle());

        // Second call: should fetch from cache
        ResumeSection section2 = cacheService.getResumeSectionById(resumeSectionId);
        Assertions.assertSame(section1, section2);

        // Verify that resumeSectionsRepository.findById was called only once
        verify(resumeSectionsRepository, times(1)).findById(resumeSectionId);
    }

    @Test
    void testGetUserById_ShouldCacheResult() {
        long userId = 1L;
        User mockUser = User.builder().id(userId).username("john_doe").name( "John").build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // First call: should query the repository
        User user1 = cacheService.getUserById(userId);
        Assertions.assertEquals("john_doe", user1.getUsername());

        // Second call: should fetch from cache
        User user2 = cacheService.getUserById(userId);
        Assertions.assertSame(user1, user2);  // Should be the same object (cached)

        // Verify that userRepository.findById was called only once
        verify(userRepository, times(1)).findById(userId);
    }


    @Test
    void testGetRoleByName_ShouldCacheResult() {
        String roleName = Constant.FREE_USER;
        Role mockRole = Role.builder().name(roleName).build();
        when(roleRepository.findByName(roleName)).thenReturn(mockRole);

        // First call: should query the repository
        Role role1 = cacheService.getRoleByName(roleName);
        Assertions.assertEquals(roleName, role1.getName());

        // Second call: should fetch from cache
        Role role2 = cacheService.getRoleByName(roleName);
        Assertions.assertSame(role1, role2);  // Should be the same object (cached)

        // Verify that roleRepository.findByName was called only once
        verify(roleRepository, times(1)).findByName(roleName);
    }
}
