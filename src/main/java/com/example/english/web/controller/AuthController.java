package com.example.english.web.controller;

import com.example.english.data.entity.User;
import com.example.english.data.model.binding.UserLoginBindingModel;
import com.example.english.data.model.binding.UserRegisterBindingModel;
import com.example.english.data.model.response.UserResponseModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.service.UserService;
import com.example.english.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Slf4j
public class AuthController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final WordService wordService;

//    @PostAuthorize("isAnonymous()")
//    @PostMapping("/login")
//    public @ResponseBody
//    ResponseEntity<UserResponseModel> login(@RequestBody UserLoginBindingModel user) {
//        Optional<UserResponseModel> userResponseModel =
//                userService.logUser(modelMapper.map(user, UserServiceModel.class))
//                        .map(u -> modelMapper.map(u, UserResponseModel.class));
//
//        return userResponseModel.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @PreAuthorize("isAnonymous()")
    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterBindingModel user, UriComponentsBuilder builder) {
        try {
            userService
                    .register(modelMapper.map(user, UserServiceModel.class))
                    .orElseThrow();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

//        return ResponseEntity.
//                created(builder.path("/register")
//                        .buildAndExpand(register.getId()).toUri()).build();
        return ResponseEntity.created(builder.path("/register").build().toUri()).build();
    }

//    @PreAuthorize(value = "hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
//    @PostMapping(value = "/logout")
//    public ResponseEntity<User> logout(@RequestBody UserRegisterBindingModel user, UriComponentsBuilder builder) {
////        User register = userService.register(
////                modelMapper.map(user,UserServiceModel.class)).get();
//        return ResponseEntity.noContent().build();
//    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user")
    public ResponseEntity<UserResponseModel> currentUser(Principal user) {

        log.info(String.valueOf(user));

        UserDetails userDetails = userService.loadUserByUsername(user.getName());

        return ResponseEntity.ok(modelMapper.map(userDetails, UserResponseModel.class));
    }
}
