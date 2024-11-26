package com.gulbalasalamov.taskmanagementsystem.service;

import com.gulbalasalamov.taskmanagementsystem.exception.CustomJwtException;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Role;
import com.gulbalasalamov.taskmanagementsystem.model.entity.User;
import com.gulbalasalamov.taskmanagementsystem.model.enums.RoleType;
import com.gulbalasalamov.taskmanagementsystem.repository.RoleRepository;
import com.gulbalasalamov.taskmanagementsystem.repository.UserRepository;
import com.gulbalasalamov.taskmanagementsystem.request.AuthRequest;
import com.gulbalasalamov.taskmanagementsystem.request.UserRequest;
import com.gulbalasalamov.taskmanagementsystem.response.AuthResponse;
import com.gulbalasalamov.taskmanagementsystem.response.UserResponse;
import com.gulbalasalamov.taskmanagementsystem.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public AuthResponse signIn(AuthRequest authRequest) {

        try {
            String email = authRequest.getEmail();
            String password = authRequest.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomJwtException("User not found", HttpStatus.BAD_REQUEST));
            String token = jwtTokenProvider.createToken(email, user.getRole().getRoleType());
            return new AuthResponse(email, token);
        } catch (AuthenticationException e) {
            throw new CustomJwtException("Invalid email/password supplied", HttpStatus.BAD_REQUEST);
        }
    }

    public UserResponse signUp(UserRequest userRequest) {

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());



        if (!userRepository.existsByUsername(userRequest.getUsername())) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            Role role = roleRepository.findByRoleType(RoleType.valueOf(userRequest.getRoleType().toUpperCase()))
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRole(role);
        } else {
            throw new CustomJwtException("User already exists", HttpStatus.CONFLICT);
        }


        User savedUser = userRepository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setUsername(savedUser.getUsername());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setRoleType(savedUser.getRole().getRoleType().name());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());

        return userResponse;
    }
}