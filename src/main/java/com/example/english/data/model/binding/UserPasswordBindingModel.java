package com.example.english.data.model.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static com.example.english.constants.UserConstants.PASSWORD_IS_REQUIRED;

@Data
@NoArgsConstructor
public class UserPasswordBindingModel {
    @NotBlank(message = PASSWORD_IS_REQUIRED)
    String oldPassword;
    @NotBlank(message = PASSWORD_IS_REQUIRED)
    String newPassword;
}
