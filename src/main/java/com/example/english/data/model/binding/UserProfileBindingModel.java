package com.example.english.data.model.binding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

import static com.example.english.constants.UserConstants.*;
import static com.example.english.constants.UserConstants.EMAIL_MUST_BE_VALID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileBindingModel {
    @NotBlank(message = USERNAME_IS_REQUIRED)
    @Length(min = 2,message = USERNAME_MUST_BE_BETWEEN_30_CHARACTERS)
    private String username;
    @NotBlank(message = EMAIL_IS_REQUIRED)
    @Email(message = EMAIL_MUST_BE_VALID)
    private String email;
    private String hobbies;
    private LocalDate birthDate;
    private String nationality;
    private String levelOfLanguage;
    private String levelExperience;
}
