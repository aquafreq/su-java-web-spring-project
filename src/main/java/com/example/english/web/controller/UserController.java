package com.example.english.web.controller;

import com.example.english.data.entity.Word;
import com.example.english.data.model.binding.WordBindingModel;
import com.example.english.service.UserService;
import com.example.english.service.WordService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final WordService wordService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping(value = "/create-word")
    public ResponseEntity<Word> addWord(@RequestBody WordBindingModel wordBindingModel, UriComponentsBuilder builder) {
        Word register = wordService.create(wordBindingModel);

        return ResponseEntity.
                created(builder.path("/api/users/create-word")
                        .buildAndExpand(register.getId()).toUri()).build();
    }
}
