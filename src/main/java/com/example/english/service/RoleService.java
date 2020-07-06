package com.example.english.service;

import com.example.english.data.entity.Role;

import java.util.List;

public interface RoleService {
    long getRoleCount();
    void seedRoles(List<Role> roles);
    Role getRoleByName(String name);
}
