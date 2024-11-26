package com.gulbalasalamov.taskmanagementsystem.model.mapper;

import com.gulbalasalamov.taskmanagementsystem.model.dto.RoleDTO;
import com.gulbalasalamov.taskmanagementsystem.model.entity.Role;
import com.gulbalasalamov.taskmanagementsystem.model.enums.RoleType;

public class RoleMapper {

    public static RoleDTO toRoleDTO(Role role) {
        if (role == null) {
            return null;
        }

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setRoleType(role.getRoleType().name());
        return roleDTO;
    }

    public static Role toRole(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }

        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setRoleType(RoleType.valueOf(roleDTO.getRoleType()));
        return role;
    }
}
