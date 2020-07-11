package com.example.english.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;

@Data
@NoArgsConstructor
public class UserProfile {
    private String username;
    private String activity;
    private Collection<String> hobbies;
    private String profilePicture;
    private WordsGame wordsGame;
    private LocalDate birthDate;
    private String nationality;
}
