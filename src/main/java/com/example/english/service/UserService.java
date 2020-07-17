package com.example.english.service;

import com.example.english.data.entity.User;
import com.example.english.data.model.response.UserProfileResponseModel;
import com.example.english.data.model.response.UserResponseModel;
import com.example.english.data.model.service.GameServiceModel;
import com.example.english.data.model.service.RoleServiceModel;
import com.example.english.data.model.service.UserProfileServiceModel;
import com.example.english.data.model.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService extends UserDetailsService {
//    Optional<UserResponseModel> logUser(UserServiceModel user);

    Optional<User> register(UserServiceModel user);

    long getCount();

    UserServiceModel giveUserRole(String userId, String roleId);

    UserServiceModel removeUserRole(String userId, String roleId);

    UserServiceModel forbidUser(String id);

    UserServiceModel permitUser(String id);

    User getUserByName(String username);

    void updateUser(User user);

    Collection<UserServiceModel> getAllUsers();

    List<String> getUserRoles(String id);

    UserServiceModel getUserById(String id);

    GameServiceModel getUserGameById(String id);

    String getUserByUsername(String id);
}
