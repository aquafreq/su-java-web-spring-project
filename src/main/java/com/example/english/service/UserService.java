package com.example.english.service;

import com.example.english.data.entity.User;
import com.example.english.data.model.response.UserResponseModel;
import com.example.english.data.model.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<UserResponseModel> logUser(UserServiceModel user);

    Optional<User> register(UserServiceModel user);
}
