package com.gulbalasalamov.taskmanagementsystem.controller;

import com.gulbalasalamov.taskmanagementsystem.request.AuthRequest;
import com.gulbalasalamov.taskmanagementsystem.request.UserRequest;
import com.gulbalasalamov.taskmanagementsystem.response.AuthResponse;
import com.gulbalasalamov.taskmanagementsystem.response.UserResponse;
import com.gulbalasalamov.taskmanagementsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.signUp(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = userService.signIn(authRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
