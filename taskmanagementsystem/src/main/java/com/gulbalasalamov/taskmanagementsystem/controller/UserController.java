package com.gulbalasalamov.taskmanagementsystem.controller;

import com.gulbalasalamov.taskmanagementsystem.model.dto.UserDTO;
import com.gulbalasalamov.taskmanagementsystem.request.SignInAuthRequest;
import com.gulbalasalamov.taskmanagementsystem.request.SignUpUserRequest;
import com.gulbalasalamov.taskmanagementsystem.response.SignInAuthResponse;
import com.gulbalasalamov.taskmanagementsystem.response.SignUpUserResponse;
import com.gulbalasalamov.taskmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpUserResponse> signUp(@RequestBody SignUpUserRequest signUpUserRequest) {
        SignUpUserResponse signUpUserResponse = userService.signUp(signUpUserRequest);
        return new ResponseEntity<>(signUpUserResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInAuthResponse> signIn(@RequestBody SignInAuthRequest signInAuthRequest) {
        SignInAuthResponse signInAuthResponse = userService.signIn(signInAuthRequest);
        return new ResponseEntity<>(signInAuthResponse, HttpStatus.OK);
    }
}
