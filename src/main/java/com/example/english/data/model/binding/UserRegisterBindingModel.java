package com.example.english.data.model.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegisterBindingModel {
    private String username;
    private String password;
    private String email;
}
