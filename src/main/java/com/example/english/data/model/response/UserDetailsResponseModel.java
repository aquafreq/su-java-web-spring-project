package com.example.english.data.model.response;

import com.example.english.data.entity.enumerations.LevelExperience;
import com.example.english.data.entity.enumerations.LevelOfLanguage;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDetailsResponseModel {
    private String username;
    private String email;
    private String isEnabled;
    private String authorities;
//    @DateTimeFormat(pattern = "dd-MMM-yyyy HH:mm:ss")
    private String registrationDate;
    private LocalDate birthDate;
    private String nationality;
    private LevelExperience levelExperience;
    private LevelOfLanguage levelOfLanguage;
    private Set<String> hobbies;

    public void setIsEnabled(boolean enabled) {
        isEnabled = String.valueOf(enabled);
    }
}
