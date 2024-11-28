package com.gulbalasalamov.taskmanagementsystem.service;

import com.gulbalasalamov.taskmanagementsystem.exception.CustomJwtException;
import com.gulbalasalamov.taskmanagementsystem.exception.InvalidRoleTypeException;
import com.gulbalasalamov.taskmanagementsystem.exception.RoleNotFoundException;
import com.gulbalasalamov.taskmanagementsystem.exception.UserAlreadyExistsException;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Role;
import com.gulbalasalamov.taskmanagementsystem.model.entity.User;
import com.gulbalasalamov.taskmanagementsystem.model.enums.RoleType;
import com.gulbalasalamov.taskmanagementsystem.model.mapper.UserMapper;
import com.gulbalasalamov.taskmanagementsystem.repository.RoleRepository;
import com.gulbalasalamov.taskmanagementsystem.repository.UserRepository;
import com.gulbalasalamov.taskmanagementsystem.request.SignInAuthRequest;
import com.gulbalasalamov.taskmanagementsystem.request.SignUpUserRequest;
import com.gulbalasalamov.taskmanagementsystem.response.SignInAuthResponse;
import com.gulbalasalamov.taskmanagementsystem.response.SignUpUserResponse;
import com.gulbalasalamov.taskmanagementsystem.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;
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

    public SignInAuthResponse signIn(SignInAuthRequest signInAuthRequest) {

        try {
            String email = signInAuthRequest.getEmail();
            String password = signInAuthRequest.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomJwtException("User not found", HttpStatus.BAD_REQUEST));
            String token = jwtTokenProvider.createToken(email, user.getRoles());
            return new SignInAuthResponse(email, token);
        } catch (AuthenticationException e) {
            throw new CustomJwtException("Invalid email/password supplied", HttpStatus.BAD_REQUEST);
        }
    }

    public SignUpUserResponse signUp(SignUpUserRequest signUpUserRequest) {
        User user = UserMapper.toUser(signUpUserRequest);
        if (userRepository.existsByUsername(signUpUserRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(signUpUserRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(signUpUserRequest.getPassword()));
        try {
            Role role = roleRepository.findByRoleType(RoleType.valueOf(signUpUserRequest.getRoleType().toUpperCase()))
                    .orElseThrow(() -> new RoleNotFoundException("Role not found"));
            role.setRoleType(RoleType.valueOf(signUpUserRequest.getRoleType().toUpperCase()));
            role.setUser(user);
            user.setRoles(Collections.singletonList(role));
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleTypeException("Invalid role type: " + signUpUserRequest.getRoleType());
        }
        User savedUser = userRepository.save(user);
        return UserMapper.toUserResponse(savedUser);
    }
}
