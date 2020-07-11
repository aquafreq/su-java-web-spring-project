package com.example.english.data.model.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterBindingModel {
    private String username;
    private String password;
    private String email;
}
