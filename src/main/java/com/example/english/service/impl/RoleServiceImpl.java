package com.example.english.service.impl;

import com.example.english.data.entity.Role;
import com.example.english.data.repository.RoleRepository;
import com.example.english.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    @Override
    public long getRoleCount() {
        return roleRepository.count();
    }

    @Override
    public void seedRoles(List<Role> roles) {
        roleRepository.saveAll(roles);
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByAuthority(name);
    }
}
