package com.example.english.data.model.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserPasswordBindingModel {
    String newPassword;
    String oldPassword;
}
