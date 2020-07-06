package com.example.english.data.model.service;

import com.example.english.data.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Data
public class UserServiceModel {
    private String username;
    private String password;
    private String email;
    private Set<RoleServiceModel> authorities = new HashSet<>();
}
