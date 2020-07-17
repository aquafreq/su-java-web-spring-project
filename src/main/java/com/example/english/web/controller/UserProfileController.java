package com.example.english.web.controller;

import com.example.english.data.entity.Word;
import com.example.english.data.model.response.WordResponseModel;
import com.example.english.data.model.service.UserProfileServiceModel;
import com.example.english.data.model.binding.UserProfileBindingModel;
import com.example.english.data.model.binding.WordBindingModel;
import com.example.english.data.model.response.GameResponseModel;
import com.example.english.data.model.response.UserGameResponseModel;
import com.example.english.data.model.response.UserProfileResponseModel;
import com.example.english.data.model.service.WordServiceModel;
import com.example.english.service.UserProfileService;
import com.example.english.service.UserService;
import com.example.english.service.GameService;
import com.example.english.service.WordService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserProfileController {
    private final GameService gameService;
    private final UserService userService;
    private final WordService wordService;
    private final UserProfileService userProfileService;
    private final ModelMapper modelMapper;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/profile/{id}/add-word")
    public ResponseEntity<Word> addWord(@RequestBody WordBindingModel wordBindingModel, UriComponentsBuilder builder) {

        WordResponseModel word =
                modelMapper.map(gameService
                                .addWord(modelMapper.map(wordService.createWord(
                                        modelMapper.map(wordBindingModel, WordServiceModel.class)),
                                        WordServiceModel.class)),
                        WordResponseModel.class);

        return ResponseEntity.
                created(builder.path("/api/users/create-word")
                        .buildAndExpand(register.getId()).toUri()).build();
    }

    //"http://localhost:8080/api/admin/user/2ec8e38a-5a8f-446b-b9c1-b4d5336839f7"
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponseModel> getUserProfile(@PathVariable String id) {
        return ResponseEntity.ok();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/profile")
    public ResponseEntity<UserProfileResponseModel> updateUserProfile(@RequestBody UserProfileBindingModel userProfileBindingModel) {
        UserProfileResponseModel resp =
                modelMapper.map(
                        userService.createProfile(
                                modelMapper.map(userProfileBindingModel, UserProfileServiceModel.class)),
                        UserProfileResponseModel.class);

        return ResponseEntity.ok(resp);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/{id}/game")
    public ResponseEntity<GameResponseModel> getUserGame(@PathVariable String id) {
        GameResponseModel gameResponseModel = modelMapper.map(userService.getUserGameById(id),
                GameResponseModel.class);

        return ResponseEntity.ok(gameResponseModel);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile/{id}/profile-picture")
    public ResponseEntity<UserProfileResponseModel> saveProfilePictureToCloud(@PathVariable String id) {
        return ResponseEntity.ok();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/{id}/profile-picture")
    public ResponseEntity<UserProfileResponseModel> getProfilePicture(@PathVariable String id) {
        return ResponseEntity.ok();
    }
}
