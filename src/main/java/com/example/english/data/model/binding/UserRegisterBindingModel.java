package com.example.english.data.model.binding;

import com.example.english.data.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import static com.example.english.constants.UserConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterBindingModel {
    @NotBlank(message = USERNAME_IS_REQUIRED)
    @Length(min = 2,max = 30,message = USERNAME_MUST_BE_BETWEEN_30_CHARACTERS)
    private String username;

    private LocalDateTime registrationDate = LocalDateTime.now();

    @NotBlank(message = PASSWORD_IS_REQUIRED)
    private String password;

    @NotBlank(message = EMAIL_IS_REQUIRED)
    @Length(max = 50,  message = MAX_CHARACTERS_FOR_EMAIL_IS_50)
    @Email(message = EMAIL_MUST_BE_VALID)
    private String email;
    private UserProfile userProfile = new UserProfile();
    private boolean isEnabled = true;
}
