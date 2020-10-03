package com.example.english.web.controller;

import com.example.english.annotations.Validate;
import com.example.english.data.model.binding.*;
import com.example.english.data.model.response.*;
import com.example.english.data.model.service.*;
import com.example.english.event.UserRegistrationEvent;
import com.example.english.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Validate
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

    @Validate
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/profile/{userId}/create-category", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryWordsResponseModel> createWordCategory(
            @RequestBody CategoryWordsBindingModel CategoryWordsBindingModel,
            @PathVariable String userId) {
        CategoryWordsServiceModel categoryWordsServiceModel = userService.addCategoryForUser(userId, CategoryWordsBindingModel.getName());

        CategoryWordsResponseModel responseModel =
                modelMapper.map(categoryWordsServiceModel, CategoryWordsResponseModel.class);
        String createdPath = String.format("/profile/%s/create-category", userId);

        return ResponseEntity
                .created(UriComponentsBuilder.fromPath(createdPath).build().toUri())
                .body(responseModel);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponseModel> getUserProfile(@PathVariable String id) {
        UserProfileResponseModel responseModel = modelMapper.map(
                userService.getUserProfileByUserId(id), UserProfileResponseModel.class);

        return ResponseEntity.ok(responseModel);
    }


    @Validate
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/profile/{id}/edit")
    public ResponseEntity<UserProfileResponseModel> updateUserProfile(
            @RequestBody UserProfileBindingModel userProfileBindingModel,
            @PathVariable String id) {

            UserProfileServiceModel map = modelMapper.map(userProfileBindingModel, UserProfileServiceModel.class);
            map.setHobbies(Arrays.asList(userProfileBindingModel.getHobbies().split(", ")));
            UserProfileServiceModel userProfileServiceModel = userService.updateProfile(map, id);

        UserProfileResponseModel resp =
                modelMapper.map(userProfileServiceModel, UserProfileResponseModel.class);

        return ResponseEntity.ok(resp);
    }

    @Validate
    @PreAuthorize("isAuthenticated()")
    @PatchMapping(value = "/profile/{id}/change-password")
    public ResponseEntity<Boolean> changeUserPassword(
            @PathVariable String id,
            @RequestBody UserPasswordBindingModel bindingModel) {
        Boolean b = userService.updatePassword(id, bindingModel.getOldPassword(), bindingModel.getNewPassword());

        return ResponseEntity.ok(b);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/profile/{id}/practice")
    public ResponseEntity<Collection<CategoryWordsResponseModel>> getUserCategoryWords(
            @PathVariable String id) {

        List<CategoryWordsResponseModel> collect = userService
                .getWordsCategoryByUserId(id)
                .stream()
                .map(x -> modelMapper.map(x, CategoryWordsResponseModel.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/profile/{id}/practice/delete-word")
    public ResponseEntity<CategoryWordsResponseModel> deleteWordInWordCategory(
            @PathVariable String id,
            @RequestBody WordCategoryDelete bindingModel) {
        CategoryWordsServiceModel map = modelMapper.map(bindingModel, CategoryWordsServiceModel.class);

        userService.deleteWordFromCategoryByUserId(id, map);

        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/profile/{id}/practice/delete-category")
    public ResponseEntity<CategoryWordsResponseModel> deleteCategory(
            @RequestBody CategoryWordDeleteBindingModel bindingModel,
            @PathVariable String id) {

//        CategoryWordsResponseModel responseModel =
//                modelMapper.map(
        userService.deleteCategoryByUserIdAndCategoryId(id, bindingModel.getId());
//                        CategoryWordsResponseModel.class);

        return ResponseEntity
                .noContent()
                .location(URI.create("/user/profile/" + id + "/practice"))
                .build();
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
}
