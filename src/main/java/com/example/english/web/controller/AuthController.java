package com.example.english.web.controller;

import com.example.english.data.entity.User;
import com.example.english.data.model.binding.UserLoginBindingModel;
import com.example.english.data.model.binding.UserRegisterBindingModel;
import com.example.english.data.model.response.UserResponseModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.service.UserService;
import com.example.english.service.WordService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashSet;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
//@CrossOrigin(value = {"http://localhost:3000"})
@RequestMapping(value = "/api/auth")
public class AuthController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final WordService wordService;

    @PostAuthorize("isAnonymous()")
    @PostMapping("/login")
    public @ResponseBody
    ResponseEntity<UserResponseModel> login(@RequestBody UserLoginBindingModel user) {
        Optional<UserResponseModel> userResponseModel =
                userService.logUser(modelMapper.map(user, UserServiceModel.class))
                        .map(u -> modelMapper.map(u, UserResponseModel.class));

        return userResponseModel.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping(value = "/register")
    public ResponseEntity<User> register(@RequestBody UserRegisterBindingModel user, UriComponentsBuilder builder) {
        User register = userService.register(
                modelMapper.map(user, UserServiceModel.class)).get();

        return ResponseEntity.
                created(builder.path("/api/auth/register")
                        .buildAndExpand(register.getId()).toUri()).build();
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping(value = "/logout")
    public ResponseEntity<User> logout(@RequestBody UserRegisterBindingModel user, UriComponentsBuilder builder) {
//        User register = userService.register(
//                modelMapper.map(user,UserServiceModel.class)).get();


        return ResponseEntity.noContent().build();
    }
}
