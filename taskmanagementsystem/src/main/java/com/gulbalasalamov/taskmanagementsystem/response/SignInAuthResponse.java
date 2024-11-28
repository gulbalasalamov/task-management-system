package com.gulbalasalamov.taskmanagementsystem.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInAuthResponse {
    private String email;
    private String token;
}
