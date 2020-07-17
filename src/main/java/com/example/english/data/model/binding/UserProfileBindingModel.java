package com.example.english.data.model.binding;

import com.example.english.data.entity.WordGame;
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
    private String activity = UserActivity.AVERAGE.name();
    private Collection<String> hobbies = new ArrayList<>();
    private String profilePicture;
    private WordServiceModel wordsGame = new WordServiceModel();
    private LocalDate birthDate;
    private String nationality;
}
