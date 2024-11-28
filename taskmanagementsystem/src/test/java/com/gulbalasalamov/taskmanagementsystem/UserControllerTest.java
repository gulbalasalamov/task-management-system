package com.gulbalasalamov.taskmanagementsystem;


import com.gulbalasalamov.taskmanagementsystem.controller.UserController;
import com.gulbalasalamov.taskmanagementsystem.model.dto.UserDTO;
import com.gulbalasalamov.taskmanagementsystem.request.SignInAuthRequest;
import com.gulbalasalamov.taskmanagementsystem.request.SignUpUserRequest;
import com.gulbalasalamov.taskmanagementsystem.response.SignInAuthResponse;
import com.gulbalasalamov.taskmanagementsystem.response.SignUpUserResponse;
import com.gulbalasalamov.taskmanagementsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @WithMockUser(username = "test_user", roles = {"USER"})
    @Test
    void shouldGetAllUsers() throws Exception {
        // Mock data
        UserDTO user1 = new UserDTO(1L, "john_doe", "john.doe@example.com", new Date(), new Date(), List.of(1L), List.of(2L), List.of(3L));
        UserDTO user2 = new UserDTO(2L, "jane_smith", "jane.smith@example.com", new Date(), new Date(), List.of(4L), List.of(5L), List.of(6L));
        when(userService.getAll()).thenReturn(List.of(user1, user2));

        // Perform GET request
        mockMvc.perform(get("/api/v1/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("john_doe")))
                .andExpect(jsonPath("$[1].username", is("jane_smith")));

        // Verify service call
        Mockito.verify(userService).getAll();
    }

    @Test
    @WithMockUser
    void shouldSignUpUser() throws Exception {
        // Mock data
        SignUpUserRequest signUpRequest = new SignUpUserRequest("john_doe", "john.doe@example.com", "password", "USER");
        SignUpUserResponse signUpResponse = new SignUpUserResponse(1L, "john_doe", "john.doe@example.com", "USER", new Date());
        when(userService.signUp(any(SignUpUserRequest.class))).thenReturn(signUpResponse);

        // Perform POST request
        mockMvc.perform(post("/api/v1/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "john_doe",
                                    "email": "john.doe@example.com",
                                    "password": "password",
                                    "roleType": "USER"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("john_doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.roleType", is("USER")));

        // Verify service call
        Mockito.verify(userService).signUp(any(SignUpUserRequest.class));
    }

    @Test
    @WithMockUser
    void shouldSignInUser() throws Exception {
        // Mock data
        SignInAuthRequest signInRequest = new SignInAuthRequest("john.doe@example.com", "password");
        SignInAuthResponse signInResponse = new SignInAuthResponse("Bearer","Bearer some-token");
        when(userService.signIn(any(SignInAuthRequest.class))).thenReturn(signInResponse);

        // Perform POST request
        mockMvc.perform(post("/api/v1/user/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "john.doe@example.com",
                                    "password": "password"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("Bearer some-token")));

        // Verify service call
        Mockito.verify(userService).signIn(any(SignInAuthRequest.class));
    }
}