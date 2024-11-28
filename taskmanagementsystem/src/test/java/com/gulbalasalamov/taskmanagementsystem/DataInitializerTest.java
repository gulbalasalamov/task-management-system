package com.gulbalasalamov.taskmanagementsystem;



import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.gulbalasalamov.taskmanagementsystem.init.DataInitializer;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Role;
import com.gulbalasalamov.taskmanagementsystem.model.enums.RoleType;
import com.gulbalasalamov.taskmanagementsystem.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.mockito.Mockito;

@SpringBootTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.gulbalasalamov.taskmanagementsystem")
public class DataInitializerTest {

    @MockBean
    private RoleRepository roleRepository;

    @SpyBean
    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(roleRepository, dataInitializer); // Mock nesneleri sıfırlama
    }

    @Test
    void shouldInitializeAdminRoleIfNotExists() throws Exception {
        when(roleRepository.findByRoleType(RoleType.ADMIN)).thenReturn(Optional.empty());
        when(roleRepository.findByRoleType(RoleType.USER)).thenReturn(Optional.of(new Role()));

        dataInitializer.run();

        verify(roleRepository, times(1)).save(new Role(null, RoleType.ADMIN, null));
        verify(dataInitializer, times(1)).run();
    }

    @Test
    void shouldInitializeUserRoleIfNotExists() throws Exception {
        when(roleRepository.findByRoleType(RoleType.USER)).thenReturn(Optional.empty());
        when(roleRepository.findByRoleType(RoleType.ADMIN)).thenReturn(Optional.of(new Role()));

        dataInitializer.run();

        verify(roleRepository, times(1)).save(new Role(null, RoleType.USER, null));
        verify(dataInitializer, times(1)).run();
    }

    @Test
    void shouldNotInitializeAdminRoleIfExists() throws Exception {
        Role adminRole = new Role(1L, RoleType.ADMIN, null);
        when(roleRepository.findByRoleType(RoleType.ADMIN)).thenReturn(Optional.of(adminRole));
        when(roleRepository.findByRoleType(RoleType.USER)).thenReturn(Optional.of(new Role()));

        dataInitializer.run();

        verify(roleRepository).findByRoleType(RoleType.ADMIN);
        verify(roleRepository, times(0)).save(new Role(null, RoleType.ADMIN, null));
    }

    @Test
    void shouldNotInitializeUserRoleIfExists() throws Exception {
        Role userRole = new Role(2L, RoleType.USER, null);
        when(roleRepository.findByRoleType(RoleType.USER)).thenReturn(Optional.of(userRole));
        when(roleRepository.findByRoleType(RoleType.ADMIN)).thenReturn(Optional.of(new Role()));

        dataInitializer.run();

        verify(roleRepository).findByRoleType(RoleType.USER);
        verify(roleRepository, times(0)).save(new Role(null, RoleType.USER, null));
    }
}
