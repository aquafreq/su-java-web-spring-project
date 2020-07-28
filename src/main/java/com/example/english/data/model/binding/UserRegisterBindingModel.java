package com.example.english.data.model.binding;

import com.example.english.data.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterBindingModel {
    private String username;
    private LocalDateTime registrationDate = LocalDateTime.now();
    private String password;
    private String email;
    private UserProfile userProfile = new UserProfile();
    private boolean isEnabled = true;
}
