package com.example.english.service.impl;

import com.example.english.data.entity.User;
import com.example.english.data.model.response.UserResponseModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.data.repository.UserRepository;
import com.example.english.service.RoleService;
import com.example.english.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException(username);
//        }
//        return new User(user.getUsername(), user.getPassword());
    }

    @Override
    public Optional<UserResponseModel> logUser(@RequestBody UserServiceModel user) {
//        UserDetails byUsername = userRepository.getByUsername(user.getUsername());
//        if (byUsername.getPassword().equals(user.getPassword())){
//        }
//        UserDetails userDetails = loadUserByUsername(user.getUsername());

        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
        User user1 = byUsername.orElseThrow();
        UserResponseModel map = modelMapper.map(user1, UserResponseModel.class);
        return Optional.of(map);
    }

    @Override
    public Optional<User> register(UserServiceModel userServiceModel) {
        userServiceModel.setPassword(passwordEncoder.encode(userServiceModel.getPassword()));

        User user = modelMapper.map(userServiceModel, User.class);

        user.getAuthorities().add(roleService.getRoleByName("ROLE_USER"));

        if (userRepository.count() == 0) {
            user.getAuthorities().add(roleService.getRoleByName("ROLE_ADMIN"));
        }

        return Optional.of(userRepository.save(user));
    }
}
