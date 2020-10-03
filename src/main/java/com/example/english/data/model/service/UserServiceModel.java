package com.example.english.data.model.service;

import com.example.english.data.entity.enumerations.LevelExperience;
import com.example.english.data.entity.enumerations.LevelOfLanguage;
import com.example.english.data.entity.enumerations.UserActivity;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import static com.example.english.constants.UserConstants.*;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserServiceModel{
    private String id;
    @NonNull
    @NotBlank(message = USERNAME_IS_REQUIRED)
    @Length(min = 2,message = USERNAME_MUST_BE_BETWEEN_30_CHARACTERS)
    private String username;

    @NonNull
    @NotBlank(message = PASSWORD_IS_REQUIRED)
    private String password;
    private boolean isEnabled;
    private UserActivity userProfileActivity;

    @NonNull
    @NotBlank(message = EMAIL_IS_REQUIRED)
    @Email(message = EMAIL_MUST_BE_VALID)
    private String email;

    private Set<RoleServiceModel> authorities = new HashSet<>();

    private LocalDateTime registrationDate;
    private LocalDate userProfileBirthDate;
    private String userProfileNationality;
    private Set<String> userProfileHobbies;
    private LevelExperience userProfileLevelExperience;
    private LevelOfLanguage userProfileLevelOfLanguage;

    public UserServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public UserServiceModel setPassword(String password) {
        this.password = password;
        return this;
    }
}
