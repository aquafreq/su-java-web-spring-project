package com.example.english.web.controller;

import com.example.english.data.entity.Word;
import com.example.english.data.model.binding.*;
import com.example.english.data.model.response.*;
import com.example.english.data.model.service.*;
import com.example.english.service.UserService;
import com.example.english.service.CategoryWordsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/profile/{userId}/{categoryId}/create-word")
    public ResponseEntity<WordResponseModel> addWordToCategory(@RequestBody WordBindingModel wordBindingModel,
                                                               @PathVariable String userId,
                                                               @PathVariable String categoryId,
                                                               UriComponentsBuilder builder) {
        WordServiceModel wordServiceModel = modelMapper.map(wordBindingModel, WordServiceModel.class);

        WordServiceModel wordServiceModel1 = userService.addWordToUserCategoryWords(wordServiceModel, categoryId, userId);

        WordResponseModel wordResponseModel =
                modelMapper.map(wordServiceModel1, WordResponseModel.class);

        String path = String.format("/user/profile/%s/%s/create-word", userId, categoryId);

        return ResponseEntity.
                created(builder.path(path)
                        .buildAndExpand(wordResponseModel.getId()).toUri()).build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/profile/{userId}/create-category", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<CategoryWordsResponseModel> createWordCategory(
            @RequestBody CategoryWordsBindingModel CategoryWordsBindingModel,
            @PathVariable String userId) {
        CategoryWordsServiceModel categoryWordsServiceModel = userService.addCategoryForUser(userId, CategoryWordsBindingModel);

        CategoryWordsResponseModel responseModel =
                modelMapper.map(categoryWordsServiceModel, CategoryWordsResponseModel.class);
        String createdPath = String.format("/profile/%s/create-category", userId);

        return ResponseEntity
                .created(UriComponentsBuilder.fromPath(createdPath).build().toUri())
                .body(responseModel);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/{userId}/categories")
    public ResponseEntity<Set<CategoryWordsResponseModel>> getUserWordCategories(@PathVariable String userId) {
        Set<CategoryWordsServiceModel> serviceModel = userService.getUserCategoriesById(userId);

        Set<CategoryWordsResponseModel> collect = serviceModel
                .stream()
                .map(x -> modelMapper.map(x, CategoryWordsResponseModel.class))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(collect);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponseModel> getUserProfile(@PathVariable String id) {
        UserProfileResponseModel responseModel = modelMapper.map(
                userService.getUserProfileById(id), UserProfileResponseModel.class);

        return ResponseEntity.ok(responseModel);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/profile/{id}/edit")
    public ResponseEntity<UserProfileResponseModel> updateUserProfile(
            @RequestBody UserProfileBindingModel userProfileBindingModel,
            @PathVariable String id) {
        UserProfileServiceModel map = modelMapper.map(userProfileBindingModel, UserProfileServiceModel.class);

        UserProfileServiceModel userProfileServiceModel = userService.updateProfile(map, id);

        UserProfileResponseModel resp =
                modelMapper.map(userProfileServiceModel, UserProfileResponseModel.class);

        return ResponseEntity.ok(resp);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT_ADMIN')")
    @GetMapping("/details/{id}")
    public ResponseEntity<UserDetailsResponseModel> getUserDetails(@PathVariable String id) {
        UserServiceModel userServiceModel = userService.getUserDetailsById(id);

        UserDetailsResponseModel map = modelMapper.map(userServiceModel, UserDetailsResponseModel.class);

        map.setAuthorities(userServiceModel
                .getAuthorities()
                .stream()
                .map(RoleServiceModel::getAuthority)
                .map(x-> x.replace("ROLE_",""))
                .collect(Collectors.joining(", ")));
        map.setIsEnabled(userServiceModel.isEnabled());
        return ResponseEntity.ok(map);
    }


    @PreAuthorize("isAuthenticated()")
    @PatchMapping(value = "/profile/{id}/change-password")
    public ResponseEntity<Boolean> getUserPassword(
            @PathVariable String id,
            @RequestBody UserPasswordBindingModel bindingModel) {
        Boolean b = userService
                .updatePassword(id, bindingModel.getOldPassword(), bindingModel.getNewPassword());

        return ResponseEntity.ok(b);
    }
}
