package com.gulbalasalamov.taskmanagementsystem.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpUserRequest {
    private String username;
    private String email;
    private String password;
    private String roleType;

}
