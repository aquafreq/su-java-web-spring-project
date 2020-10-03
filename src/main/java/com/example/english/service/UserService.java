package com.example.english.service;

import com.example.english.data.entity.User;
import com.example.english.data.model.service.CategoryWordsServiceModel;
import com.example.english.data.model.service.UserProfileServiceModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.data.model.service.WordServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService extends UserDetailsService {
//    Optional<UserResponseModel> logUser(UserServiceModel user);

    Optional<User> register(UserServiceModel user);

    long getCount();

    UserServiceModel giveUserRole(String userId, String roleId);

    UserServiceModel removeUserRole(String userId, String roleId);

    UserServiceModel forbidUser(String id);

    UserServiceModel permitUser(String id);

    UserServiceModel getUserByName(String username);

    void updateUser(User user);

    Collection<UserServiceModel> getAllUsers();

    List<String> getUserRoles(String id);

    UserServiceModel getUserById(String id);

    String getUserByUsername(String username);

    UserProfileServiceModel saveProfile(UserProfileServiceModel userProfileServiceModel);
    void deleteProfile(String id);

    UserProfileServiceModel updateProfile(UserProfileServiceModel userProfileServiceModel,String id);

    UserProfileServiceModel getUserProfileByUserId(String id);

    CategoryWordsServiceModel addCategoryForUser(String userId, String categoryName);

    Set<CategoryWordsServiceModel> getUserCategoriesById(String userId);

    WordServiceModel addWordToUserCategoryWords(WordServiceModel wordServiceModel, String categoryId, String userId);

    boolean updatePassword(String id, String oldPassword, String newPassword);

    UserServiceModel getUserDetailsById(String id);

    Collection<CategoryWordsServiceModel> getWordsCategoryByUserId(String id);

    CategoryWordsServiceModel deleteWordFromCategoryByUserId(String id,CategoryWordsServiceModel categoryWordsServiceModel);

    void deleteCategoryByUserIdAndCategoryId(String id, String name);

    void updateUserActivity();

    String getUserIdByUsername(String name);
}
