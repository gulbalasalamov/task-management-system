package com.gulbalasalamov.taskmanagementsystem.init;

import com.gulbalasalamov.taskmanagementsystem.model.entity.Role;
import com.gulbalasalamov.taskmanagementsystem.model.enums.RoleType;
import com.gulbalasalamov.taskmanagementsystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByRoleType(RoleType.ADMIN).isEmpty()){
            roleRepository.save(new Role(null,RoleType.ADMIN,null));
        }
        if (roleRepository.findByRoleType(RoleType.USER).isEmpty()){
            roleRepository.save(new Role(null,RoleType.USER,null));
        }
    }
}