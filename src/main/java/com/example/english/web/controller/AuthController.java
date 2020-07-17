package com.example.english.web.controller;

import com.example.english.data.model.binding.UserRegisterBindingModel;
import com.example.english.data.model.response.UserResponseModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.service.UserService;
import com.example.english.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Slf4j
public class AuthController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final GameService gameService;

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

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseModel> currentUser(Principal user, HttpServletRequest request, HttpServletResponse response) {
        log.info(String.valueOf(user));
        UserDetails userDetails = userService.loadUserByUsername(user.getName());
        return ResponseEntity.ok(modelMapper.map(userDetails, UserResponseModel.class));
    }
}
