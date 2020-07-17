package com.example.english.service;

import com.example.english.data.model.service.UserProfileServiceModel;

public interface UserProfileService {
    UserProfileServiceModel saveProfile(UserProfileServiceModel userProfileServiceModel);
    void deleteProfile(String id);

    UserProfileService updateProfile(UserProfileService userProfileService);
}
