package com.gulbalasalamov.taskmanagementsystem.model.mapper;

import com.gulbalasalamov.taskmanagementsystem.model.dto.UserDTO;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Comment;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Role;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Task;
import com.gulbalasalamov.taskmanagementsystem.model.entity.User;
import com.gulbalasalamov.taskmanagementsystem.request.SignUpUserRequest;
import com.gulbalasalamov.taskmanagementsystem.response.SignUpUserResponse;

import java.util.ArrayList;
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

        userDTO.setRoleIds(user.getRoles() != null
                ? user.getRoles().stream().map(Role::getId).collect(Collectors.toList())
                : new ArrayList<>());

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
        if (userDTO.getRoleIds() != null) {
            user.setRoles(userDTO.getRoleIds().stream().map(roleId -> {
                Role role = new Role();
                role.setId(roleId);
                return role;
            }).collect(Collectors.toList()));
        }

        return user;
    }

    public static User toUser(SignUpUserRequest signUpUserRequest) {
        User user = new User();
        user.setUsername(signUpUserRequest.getUsername());
        user.setEmail(signUpUserRequest.getEmail());
        user.setPassword(signUpUserRequest.getPassword());
        return user;
    }

    public static SignUpUserResponse toUserResponse(User user) {
        SignUpUserResponse signUpUserResponse = new SignUpUserResponse();
        signUpUserResponse.setId(user.getId());
        signUpUserResponse.setUsername(user.getUsername());
        signUpUserResponse.setEmail(user.getEmail());
        signUpUserResponse.setRoleType(user.getRoles() != null && !user.getRoles().isEmpty()
                ? user.getRoles().get(0).getRoleType().name()
                : null);
        signUpUserResponse.setCreatedAt(user.getCreatedAt());
        return signUpUserResponse;
    }
}
