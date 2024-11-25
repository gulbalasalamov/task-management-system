package com.gulbalasalamov.taskmanagementsystem.model.mapper;

import com.gulbalasalamov.taskmanagementsystem.model.dto.CommentDTO;
import com.gulbalasalamov.taskmanagementsystem.model.dto.TaskDTO;
import com.gulbalasalamov.taskmanagementsystem.model.dto.UserDTO;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Comment;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Role;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Task;
import com.gulbalasalamov.taskmanagementsystem.model.entity.User;
import com.gulbalasalamov.taskmanagementsystem.model.enums.RoleName;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setRoleId(user.getRole() != null ? user.getRole().getId() : null);

        userDTO.setTaskIds(user.getTasks() != null
                ? user.getTasks().stream().map(Task::getId).collect(Collectors.toList())
                : new ArrayList<>());

        userDTO.setCommentIds(user.getComments() != null
                ? user.getComments().stream().map(Comment::getId).collect(Collectors.toList())
                : new ArrayList<>());

        return userDTO;
    }

    public static User toUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setUpdatedAt(userDTO.getUpdatedAt());

        // Role conversion
        if (userDTO.getRoleId() != null) {
            Role role = new Role();
            role.setId(userDTO.getRoleId());
            user.setRole(role);
        }

        return user;
    }
}
