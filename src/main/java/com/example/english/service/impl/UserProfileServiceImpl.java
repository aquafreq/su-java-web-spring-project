package com.example.english.service.impl;

import com.example.english.data.entity.UserProfile;
import com.example.english.data.model.service.UserProfileServiceModel;
import com.example.english.data.repository.UserProfileRepository;
import com.example.english.service.UserProfileService;
import com.example.english.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository repository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public UserProfileServiceModel saveProfile(UserProfileServiceModel userProfileServiceModel) {
        return modelMapper.map(
                repository.save(
                        modelMapper.map(userProfileServiceModel, UserProfile.class)),
                UserProfileServiceModel.class);
    }

    @Override
    public void deleteProfile(String id) {
        repository.deleteById(id);
    }

    @Override
    public UserProfileService updateProfile(UserProfileService userProfileService) {
        UserProfile map = modelMapper.map(userProfileService, UserProfile.class);
        repository.save(map);
    }
}
