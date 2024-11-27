package com.gulbalasalamov.taskmanagementsystem.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpUserResponse {
    private Long id;
    private String username;
    private String email;
    private String roleType;
    private Date createdAt;
}
