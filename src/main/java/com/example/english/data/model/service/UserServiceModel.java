package com.example.english.data.model.service;

import com.example.english.data.entity.Role;
import com.example.english.service.RoleService;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Data
@RequiredArgsConstructor
public class UserServiceModel{
    private String id;
    @NonNull
    private String username;
    @NonNull
    private String password;
    private boolean isEnabled;
    @NonNull
    private String email;

    private Set<RoleServiceModel> authorities = new HashSet<>();
}
