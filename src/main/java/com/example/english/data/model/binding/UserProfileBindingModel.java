package com.example.english.data.model.binding;

import com.example.english.data.entity.enumerations.UserActivity;
import com.example.english.data.model.service.WordServiceModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
public class UserProfileBindingModel {
    private String username;
    private String email;
    private Collection<String> hobbies;
    private LocalDate birthDate;
    private String nationality;
    private String levelOfLanguage;
    private String levelExperience;
}
