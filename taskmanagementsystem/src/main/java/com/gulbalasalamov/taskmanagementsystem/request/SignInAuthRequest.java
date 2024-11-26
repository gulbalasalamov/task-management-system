package com.gulbalasalamov.taskmanagementsystem.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInAuthRequest {
    private String email;
    private String password;
}
