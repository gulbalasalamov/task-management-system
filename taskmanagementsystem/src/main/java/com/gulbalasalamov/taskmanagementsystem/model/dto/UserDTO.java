package com.gulbalasalamov.taskmanagementsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Date createdAt;
    private Date updatedAt;
    private List<Long> commentIds;
    private List<Long> taskIds;
    private List<Long> roleIds;
    //private Long roleId;
}
