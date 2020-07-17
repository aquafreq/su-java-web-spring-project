package com.example.english.data.model.service;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;

@Data
@NoArgsConstructor
public class UserProfileServiceModel {
    private String id;
    private String username;
    private String activity;
    private Collection<String> hobbies;
    private String profilePicture;
    private GameServiceModel wordsGame;
    private LocalDate birthDate;
    private String nationality;
}
