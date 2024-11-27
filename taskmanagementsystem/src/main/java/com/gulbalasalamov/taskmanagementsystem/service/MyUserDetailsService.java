package com.gulbalasalamov.taskmanagementsystem.service;

import com.gulbalasalamov.taskmanagementsystem.exception.EmailNotFoundException;
import com.gulbalasalamov.taskmanagementsystem.model.entity.User;
import com.gulbalasalamov.taskmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
//                .orElse(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .authorities(user.getRoles())
                .accountLocked(false)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

//    public UserDetails loadUserByEmail(String email) throws EmailNotFoundException {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(()-> new EmailNotFoundException("User with email: " + email + " not found"));
//
//        return org.springframework.security.core.userdetails.User
//                .withUsername(email)
//                .password(user.getPassword())
//                .authorities(user.getRoles())
//                .accountLocked(false)
//                .accountExpired(false)
//                .accountLocked(false)
//                .credentialsExpired(false)
//                .disabled(false)
//                .build();
//    }
}