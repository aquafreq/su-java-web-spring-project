package com.example.english.web.controller;

import com.example.english.data.entity.Word;
import com.example.english.data.model.binding.UserProfileBindingModel;
import com.example.english.data.model.binding.WordBindingModel;
import com.example.english.data.model.response.UserProfileResponseModel;
import com.example.english.data.model.response.WordResponseModel;
import com.example.english.data.model.service.UserProfileServiceModel;
import com.example.english.data.model.service.WordServiceModel;
import com.example.english.service.UserService;
import com.example.english.service.CategoryWordsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CategoryWordsService categoryWordsService;
    private final ModelMapper modelMapper;

    //da sa prai
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/profile/{id}/add-word")
    public ResponseEntity<Word> addWordToCategory(@RequestBody WordBindingModel wordBindingModel,
                                                  @PathVariable(value = "id") String wordId,
                                                  String categoryId,
                                                  UriComponentsBuilder builder) {
        WordServiceModel wordServiceModel = modelMapper.map(wordBindingModel, WordServiceModel.class);

        WordResponseModel wordResponseModel = modelMapper.map(
                categoryWordsService.addWordToCategory(wordServiceModel),
                WordResponseModel.class);

        return ResponseEntity.
                created(builder.path("/api/users/create-word")
                        .buildAndExpand(wordResponseModel.getId()).toUri()).build();
    }

    //"http://localhost:8080/api/admin/user/2ec8e38a-5a8f-446b-b9c1-b4d5336839f7"
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponseModel> getUserProfile(@PathVariable String id) {

        UserProfileResponseModel responseModel = modelMapper.map(
                modelMapper.map(userService.getUserProfileById(id), UserProfileServiceModel.class),
                UserProfileResponseModel.class);

        return ResponseEntity.ok(responseModel);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/profile")
    public ResponseEntity<UserProfileResponseModel> updateUserProfile(@RequestBody UserProfileBindingModel userProfileBindingModel) {
        UserProfileResponseModel resp =
                modelMapper.map(
                        userService.updateProfile(
                                modelMapper.map(userProfileBindingModel, UserProfileServiceModel.class)),
                        UserProfileResponseModel.class);

        return ResponseEntity.ok(resp);
    }


    //cloudinary java ?
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile/{id}/profile-picture")
    public ResponseEntity<UserProfileResponseModel> saveProfilePictureToCloud(@PathVariable String id) {

//        return ResponseEntity.ok();
        return null;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/{id}/profile-picture")
    public ResponseEntity<UserProfileResponseModel> getProfilePicture(@PathVariable String id) {
//        return ResponseEntity.ok();
        return null;
    }
}
