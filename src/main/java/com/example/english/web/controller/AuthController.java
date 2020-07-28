package com.example.english.web.controller;

import com.example.english.data.entity.User;
import com.example.english.data.model.binding.UserRegisterBindingModel;
import com.example.english.data.model.response.UserResponseModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.service.UserService;
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

    @PostMapping("/register")
    public ResponseEntity<UserResponseModel> registerUser(
            @RequestBody UserRegisterBindingModel bindingModel) {
        User user = userService.register(
                modelMapper.map(bindingModel, UserServiceModel.class)).orElseThrow();

        UserResponseModel responseModel = modelMapper.map(user, UserResponseModel.class);

        return ResponseEntity
                .created(UriComponentsBuilder.fromPath("/register").build().toUri())
                .body(responseModel);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<UserResponseModel> getUser(Principal principal) {
//        principal.getName()
        UserServiceModel userByName = userService.getUserByName(principal.getName());
        UserResponseModel responseModel = modelMapper.map(userByName, UserResponseModel.class);
        return ResponseEntity.ok(responseModel);
    }
}
