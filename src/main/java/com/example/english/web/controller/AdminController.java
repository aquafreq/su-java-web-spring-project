package com.example.english.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class AdminController {
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/create/category")
    public ResponseEntity.BodyBuilder createCategory(String category){

        return ResponseEntity.status(HttpStatus.CREATED);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/create/exercise")
    public ResponseEntity.BodyBuilder createExercise(String exercise){
        //choose category, add exercises to it, grammar
        return ResponseEntity.status(HttpStatus.CREATED);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/roles")
    public ResponseEntity.BodyBuilder roles(String user, String role){
        //todo give remove role to users
        //display users with their current roles
        return ResponseEntity.status(HttpStatus.CREATED);
    }

}
