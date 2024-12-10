package com.gulbalasalamov.taskmanagementsystem.service;

import com.gulbalasalamov.taskmanagementsystem.exception.EmailNotFoundException;
import com.gulbalasalamov.taskmanagementsystem.model.entity.User;
import com.gulbalasalamov.taskmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * Service for loading user-specific data for authentication
 */
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Loads the user by username (email) for authentication process
     *
     * @param username the username (email) of the user.
     * @return the UserDetails of the user
     * @throws UsernameNotFoundException if the user is not found with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_"+role.getRoleType().name())) // RoleType enum değerleri ile SimpleGrantedAuthority
                        .collect(Collectors.toList()))
                .accountLocked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
