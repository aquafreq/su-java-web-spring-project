package com.example.english.web.controller;

import com.example.english.annotations.Validate;
import com.example.english.data.entity.User;
import com.example.english.data.model.binding.UserRegisterBindingModel;
import com.example.english.data.model.response.UserResponseModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.data.repository.UserRepository;
import com.example.english.event.UserPublisher;
import com.example.english.event.UserRegistrationEvent;
import com.example.english.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.NoSuchElementException;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Slf4j
@Profile("!test")
public class AuthController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final UserPublisher userPublisher;

    @Validate
    @PostMapping("/register")
    public ResponseEntity<UserResponseModel> registerUser(
            @RequestBody UserRegisterBindingModel bindingModel) {
        //FIXME
        User toBeFixed = userService.register(modelMapper.map(bindingModel, UserServiceModel.class))
                .orElse(null);

        UserResponseModel responseModel = modelMapper.map(toBeFixed, UserResponseModel.class);

        //disabled because hosting in heroku breaks the email sendings
        //userPublisher.publishEvent(responseModel);

        return ResponseEntity
                .created(UriComponentsBuilder.fromPath("/register").build().toUri())
                .body(responseModel);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<UserResponseModel> getUser(Principal principal) {
        UserServiceModel userByName = userService.getUserById(principal.getName());
        UserResponseModel responseModel = modelMapper.map(userByName, UserResponseModel.class);
        return ResponseEntity.ok(responseModel);
    }

//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/{id}")
//    public ResponseEntity<UserResponseModel> getUserById(@PathVariable String id) {
//        UserServiceModel userById = userService.getUserById(id);
//        UserResponseModel responseModel = modelMapper.map(userById, UserResponseModel.class);
//        return ResponseEntity.ok(responseModel);
//    }
}