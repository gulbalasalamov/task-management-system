package com.gulbalasalamov.taskmanagementsystem;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.gulbalasalamov.taskmanagementsystem.exception.CustomJwtException;
import com.gulbalasalamov.taskmanagementsystem.exception.UserAlreadyExistsException;
import com.gulbalasalamov.taskmanagementsystem.exception.RoleNotFoundException;
import com.gulbalasalamov.taskmanagementsystem.model.dto.UserDTO;

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
import com.gulbalasalamov.taskmanagementsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    private User user;
    private Role role;
    private UserDTO userDTO;
    private SignInAuthRequest signInAuthRequest;
    private SignUpUserRequest signUpUserRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = createUser(1L, "testUser", "testUser@gmail.com");
        role = createRole(1L, RoleType.USER, user);
        userDTO = createUserDTO(user);
        signInAuthRequest = createSignInAuthRequest(user.getEmail(), "password");
        signUpUserRequest = createSignUpUserRequest("newUser", "newUser@gmail.com", "password", RoleType.USER.name());
    }

    private User createUser(Long id, String username, String email) {
        return new User(id, username, email, "password", new Date(), new Date(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    private Role createRole(Long id, RoleType roleType, User user) {
        Role role = new Role();
        role.setId(id);
        role.setRoleType(roleType);
        role.setUser(user);
        return role;
    }

    private UserDTO createUserDTO(User user) {
        return UserMapper.toUserDTO(user);
    }

    private SignInAuthRequest createSignInAuthRequest(String email, String password) {
        return new SignInAuthRequest(email, password);
    }

    private SignUpUserRequest createSignUpUserRequest(String username, String email, String password, String roleType) {
        return new SignUpUserRequest(username, email, password, roleType);
    }

    @Test
    void shouldGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDTO> result = userService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("testUser");
    }

    @Test
    void shouldSignIn() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.createToken(user.getEmail(), user.getRoles())).thenReturn("token");

        SignInAuthResponse result = userService.signIn(signInAuthRequest);

        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getToken()).isEqualTo("token");
    }

    @Test
    void shouldThrowExceptionWhenSignInFails() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid email/password") {});

        assertThrows(CustomJwtException.class, () -> userService.signIn(signInAuthRequest));
    }

    @Test
    void shouldSignUp() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(roleRepository.findByRoleType(any(RoleType.class))).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        SignUpUserResponse result = userService.signUp(signUpUserRequest);

        assertThat(result.getUsername()).isEqualTo("testUser");
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUsernameExists() {
        when(userRepository.existsByUsername(signUpUserRequest.getUsername())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.signUp(signUpUserRequest));
    }

    @Test
    void shouldThrowExceptionWhenEmailExists() {
        when(userRepository.existsByEmail(signUpUserRequest.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.signUp(signUpUserRequest));
    }

    @Test
    void shouldThrowExceptionWhenRoleNotFound() {
        when(roleRepository.findByRoleType(any(RoleType.class))).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> userService.signUp(signUpUserRequest));
    }
}
