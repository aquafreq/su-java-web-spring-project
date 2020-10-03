package com.example.english.data.model.service;

import com.example.english.data.entity.enumerations.LevelExperience;
import com.example.english.data.entity.enumerations.LevelOfLanguage;
import com.example.english.data.entity.enumerations.UserActivity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
public class UserProfileServiceModel {
    private String id;
    private String username;
    private String email;
    private UserActivity activity;
    private LevelExperience levelExperience;
    private LevelOfLanguage levelOfLanguage;
    private LocalDate birthDate;
    private String nationality;
    private List<String> hobbies;

    public UserProfileServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserProfileServiceModel setEmail(String email) {
        this.email = email;
        return this;
    }
}
