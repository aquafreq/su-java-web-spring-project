package com.example.english.service.impl;

import com.example.english.data.entity.Role;
import com.example.english.data.entity.User;
import com.example.english.data.model.response.UserResponseModel;
import com.example.english.data.model.service.RoleServiceModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.data.repository.UserRepository;
import com.example.english.service.RoleService;
import com.example.english.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

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
    public Optional<User> register(UserServiceModel userServiceModel) throws IllegalArgumentException {

        if (userRepository.findByUsername(userServiceModel.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Such username already exists!");
        }

        if (userRepository.findByEmail(userServiceModel.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Such email already exists!");
        }

        userServiceModel.setPassword(passwordEncoder.encode(userServiceModel.getPassword()));

        User user = modelMapper.map(userServiceModel, User.class);

        user.getAuthorities().add(roleService.getRoleByName("ROLE_USER"));

        if (userRepository.count() == 0) {
            user.getAuthorities().add(roleService.getRoleByName("ROLE_ADMIN"));
        }

        return Optional.of(userRepository.save(user));
    }

    @Override
    public long getCount() {
        return userRepository.count();
    }


    @Override
    public UserServiceModel giveUserRole(String roleId, String userId) {
        Role userRole = modelMapper.map(roleService.getRoleById(roleId),
                Role.class);

        User user = userRepository.findById(userId).orElseThrow();
        user.getAuthorities().add(userRole);

        return modelMapper.map(userRepository.save(user), UserServiceModel.class);
    }

    @Override
    public UserServiceModel removeUserRole(String roleId, String userId) {
        Role userRole = modelMapper.map(roleService.getRoleById(roleId), Role.class);
        User user = userRepository.findById(userId).orElseThrow();
        user.getAuthorities().remove(userRole);
        return modelMapper.map(userRepository.save(user),
                UserServiceModel.class);
    }

    @Override
    public UserServiceModel forbidUser(String id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setEnabled(false);
        return modelMapper.map(userRepository.save(user), UserServiceModel.class);
    }

    @Override
    public UserServiceModel permitUser(String id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setEnabled(true);
        return modelMapper.map(userRepository.save(user), UserServiceModel.class);
    }

    @Override
    public User getUserByName(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Collection<UserServiceModel> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(x -> modelMapper.map(x, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getUserRoles(String id) {
        return userRepository.findById(id)
                .orElseThrow()
                .getAuthorities()
                .stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList());
    }

    @Override
    public UserServiceModel getUserById(String id) {
        return modelMapper.map(
                userRepository.findById(id).orElseThrow(),
                UserServiceModel.class);
    }
}
