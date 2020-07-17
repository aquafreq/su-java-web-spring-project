package com.example.english.data.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;

@Data
@NoArgsConstructor
public class UserProfileResponseModel {
    private String username;
    private String activity;
    private Collection<String> hobbies;
    private String profilePicture;
    private GameResponseModel game;
    private LocalDate birthDate;
    private String nationality;

}
