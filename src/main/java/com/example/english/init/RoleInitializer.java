package com.example.english.init;

import com.example.english.data.entity.Role;
import com.example.english.data.entity.enumerations.RoleEnum;
import com.example.english.service.RoleService;
import com.example.english.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {
    private final RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        if (roleService.getRoleCount() == 0) {
            roleService.seedRoles(
                    Arrays.stream(RoleEnum.values()).map(r -> new Role(r.name())).collect(Collectors.toList())
            );
        }
    }
}
