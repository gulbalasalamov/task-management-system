package com.gulbalasalamov.taskmanagementsystem.controller;

import com.gulbalasalamov.taskmanagementsystem.request.SignInAuthRequest;
import com.gulbalasalamov.taskmanagementsystem.request.SIgnUpUserRequest;
import com.gulbalasalamov.taskmanagementsystem.response.AuthResponse;
import com.gulbalasalamov.taskmanagementsystem.response.UserResponse;
import com.gulbalasalamov.taskmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody SIgnUpUserRequest SIgnUpUserRequest) {
        UserResponse userResponse = userService.signUp(SIgnUpUserRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody SignInAuthRequest signInAuthRequest) {
        AuthResponse authResponse = userService.signIn(signInAuthRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
