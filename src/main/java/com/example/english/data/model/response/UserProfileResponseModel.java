package com.example.english.data.model.response;

import com.example.english.data.entity.enumerations.UserActivity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;

@Data
@NoArgsConstructor
public class UserProfileResponseModel {
    private Collection<String> hobbies;
    private LocalDate birthDate;
    private String nationality;
    private String levelOfLanguage;
    private String levelExperience;
    private UserActivity activity;
    private String username;
    private String email;
}
