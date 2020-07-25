package com.example.english.data.model.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserChangePasswordBindingModel {
    private String oldPassword;
    private String newPassword;
}
