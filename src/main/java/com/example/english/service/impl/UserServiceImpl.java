package com.example.english.service.impl;

import com.example.english.data.entity.*;
import com.example.english.data.entity.enumerations.UserActivity;
import com.example.english.data.model.service.*;
import com.example.english.data.repository.UserRepository;
import com.example.english.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.english.constants.CategoryConstants.NO_SUCH_CATEGORY;
import static com.example.english.constants.UserConstants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final CategoryWordsService categoryWordsService;
    private final WordService wordService;
    private final ModelMapper modelMapper;
    private final LogService logService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
    }

    @Override
    public Optional<User> register(UserServiceModel userServiceModel) throws IllegalArgumentException {

        if (userRepository.findByUsername(userServiceModel.getUsername()).isPresent()) {
            throw new IllegalArgumentException(SUCH_USERNAME_ALREADY_EXISTS);
        }

        if (userRepository.findByEmail(userServiceModel.getEmail()).isPresent()) {
            throw new IllegalArgumentException(SUCH_EMAIL_ALREADY_EXISTS);
        }

        userServiceModel.setPassword(passwordEncoder.encode(userServiceModel.getPassword()));

        User user = modelMapper.map(userServiceModel, User.class);
        user.setEnabled(true);
        user.getAuthorities().add(roleService.getRoleByName("ROLE_USER"));
        user.setRegistrationDate(LocalDateTime.now());

        if (userRepository.count() == 0) {
            Arrays.asList(
                    roleService.getRoleByName("ROLE_ADMIN"),
                    roleService.getRoleByName("ROLE_ROOT_ADMIN"),
                    roleService.getRoleByName("ROLE_MODERATOR")
            ).forEach(r -> {
                user.getAuthorities().add(r);
            });
        }

        //event
        return Optional.of(userRepository.saveAndFlush(user));
    }

    @Override
    public long getCount() {
        return userRepository.count();
    }

    @Override
    public UserServiceModel giveUserRole(String userId, String roleId) {
        Role userRole = modelMapper.map(roleService.getRoleById(roleId),
                Role.class);

        User user = userRepository.findById(userId).orElseThrow();
        user.getAuthorities().add(userRole);

        return modelMapper.map(userRepository.save(user), UserServiceModel.class);
    }

    @Override
    public UserServiceModel removeUserRole(String userId, String roleId) {
        User user = userRepository.findById(userId).orElseThrow();

        user.setAuthorities(user.getAuthorities()
                .stream()
                .filter(a -> !a.getId().equals(roleId))
                .collect(Collectors.toSet()));

        return modelMapper.map(userRepository.save(user),
                UserServiceModel.class);
    }

    @Override
    public UserServiceModel forbidUser(String id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setEnabled(false);
        return modelMapper.map(userRepository.save(user), UserServiceModel.class);
    }

    @Override
    public UserServiceModel permitUser(String id) {
        User user = userRepository.findById(id).orElseThrow();

        user.setEnabled(true);
        return modelMapper.map(userRepository.save(user), UserServiceModel.class);
    }

    @Override
    public UserServiceModel getUserByName(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Collection<UserServiceModel> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(x -> modelMapper.map(x, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getUserRoles(String id) {
        return userRepository.findById(id)
                .orElseThrow()
                .getAuthorities()
                .stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList());
    }

    @Override
    public UserServiceModel getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow();

        return modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public String getUserByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .map(User::getUsername)
                .orElseThrow();
    }

    @Override
    public UserProfileServiceModel saveProfile(UserProfileServiceModel userProfileServiceModel) {
        User user = userRepository
                .findByUsername(userProfileServiceModel.getUsername())
                .orElseThrow();

        UserProfile map = modelMapper.map(userProfileServiceModel, UserProfile.class);
        user.setUserProfile(map);
        UserProfile userProfile = userRepository.save(user).getUserProfile();

        return modelMapper.map(userProfile, UserProfileServiceModel.class)
                .setUsername(user.getUsername())
                .setEmail(user.getEmail());
    }

    @Override
    public void deleteProfile(String id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUserProfile(null);
        userRepository.save(user);
    }

    @Override
    public UserProfileServiceModel updateProfile(UserProfileServiceModel userProfileServiceModel, String id) {
        User user = userRepository.findById(id).orElseThrow();

        String editUsername = userProfileServiceModel.getUsername();
        String editEmail = userProfileServiceModel.getEmail();

        if (!editUsername.equals(user.getUsername())) {
            user.setUsername(editUsername);
        }


        if (!editEmail.equals(user.getEmail())) {
            user.setEmail(editEmail);
        }

        Set<CategoryWords> oldCategoryWords = user.getUserProfile().getCategoriesWithWords();
        UserActivity activity = user.getUserProfile().getActivity();
        int activityPoints = user.getUserProfile().getActivityPoints();

        user.setUserProfile(modelMapper.map(userProfileServiceModel, UserProfile.class));

        user.getUserProfile()
                .setActivityPoints(activityPoints)
                .setActivity(activity)
                .setCategoriesWithWords(oldCategoryWords);

        User save = userRepository.save(user);

        return modelMapper.map(save.getUserProfile(), UserProfileServiceModel.class)
                .setUsername(user.getUsername())
                .setEmail(user.getEmail());
    }

    @Override
    public UserProfileServiceModel getUserProfileByUserId(String id) {
        User user = userRepository.findById(id).orElseThrow();

        UserProfileServiceModel map = modelMapper.map(user.getUserProfile(), UserProfileServiceModel.class);
        return map.setUsername(user.getUsername()).setEmail(user.getEmail());
    }

    @Override
    public CategoryWordsServiceModel addCategoryForUser(String userId, String categoryName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(NO_SUCH_USER));

        if (user.getUserProfile().getCategoriesWithWords()
                .stream().anyMatch(c -> c.getName().equals(categoryName)))
            throw new IllegalArgumentException(categoryName + " already exists, add another one or delete this one!");

        CategoryWordsServiceModel serviceModel =
                categoryWordsService.addCategory(categoryName);

        CategoryWords map = modelMapper.map(serviceModel, CategoryWords.class);

        Set<CategoryWords> categoryWords = user.getUserProfile().getCategoriesWithWords();
        categoryWords.add(map);
        userRepository.saveAndFlush(user);

        return modelMapper.map(map, CategoryWordsServiceModel.class);
    }

    @Override
    public Set<CategoryWordsServiceModel> getUserCategoriesById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow()
                .getUserProfile()
                .getCategoriesWithWords()
                .stream()
                .map(x -> modelMapper.map(x, CategoryWordsServiceModel.class))
                .collect(Collectors.toSet());
    }

    @Override
    public WordServiceModel addWordToUserCategoryWords(WordServiceModel wordServiceModel, String categoryId, String userId) {
        User user = userRepository.findById(userId).orElseThrow();

        if (user
                .getUserProfile()
                .getCategoriesWithWords()
                .stream()
                .anyMatch(c -> {
                    if (c.getId().equals(categoryId)) {
                        return c.getWords()
                                .stream()
                                .anyMatch(w -> w.getName().equals(wordServiceModel.getName()));
                    }

                    return false;
                }))
            throw new IllegalArgumentException("The word exists in this category already!");

        Word map1 = modelMapper.map(wordService.createWord(wordServiceModel), Word.class);

        CategoryWords categoryWords = user.getUserProfile()
                .getCategoriesWithWords()
                .stream()
                .filter(c -> c.getId().equals(categoryId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(NO_SUCH_CATEGORY));

        categoryWords
                .getWords()
                .add(map1);

        userRepository.save(user);

        return modelMapper.map(map1, WordServiceModel.class);
    }

    @Override
    public boolean updatePassword(String id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id).orElseThrow();

        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));

            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public UserServiceModel getUserDetailsById(String id) {
        User user = userRepository.findById(id).orElseThrow();
        return modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public Collection<CategoryWordsServiceModel> getWordsCategoryByUserId(String id) {
        return userRepository
                .findById(id)
                .orElseThrow()
                .getUserProfile()
                .getCategoriesWithWords()
                .stream()
                .map(x -> modelMapper.map(x, CategoryWordsServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryWordsServiceModel deleteWordFromCategoryByUserId(String id, CategoryWordsServiceModel serviceModel) {
        User user = userRepository.findById(id).orElseThrow();

        Set<CategoryWords> categoryWords = user
                .getUserProfile()
                .getCategoriesWithWords()
                .stream()
                .peek(c -> {
                    if (c.getName().equals(serviceModel.getName())) {
                        categoryWordsService.removeWordFromCategory(c.getId(), serviceModel.getWordName());
                    }
                })
                .collect(Collectors.toSet());

        user.getUserProfile().setCategoriesWithWords(categoryWords);

        CategoryWords category = getCategoryFromSavedUser(serviceModel.getName(), user);

        return modelMapper.map(category, CategoryWordsServiceModel.class);
    }

    @Override
    public void deleteCategoryByUserIdAndCategoryId(String id, String categoryId) {
        User user = userRepository.findById(id).orElseThrow();

        Set<CategoryWords> collect = user
                .getUserProfile()
                .getCategoriesWithWords()
                .stream()
                .filter(c -> {

                    if (c.getId().equals(categoryId)) {
                        categoryWordsService.removeCategoryById(categoryId);
                        return false;
                    }

                    return true;
                })
                .collect(Collectors.toSet());

        user.getUserProfile().setCategoriesWithWords(collect);
        userRepository.save(user);
    }

    @Override
    public void updateUserActivity() {
        userRepository
                .findAll()
                .stream()
                .map(this::calculateUserActivity)
                .forEach(userRepository::save);
    }

    @Override
    public String getUserIdByUsername(String name) {
        return userRepository.findByUsername(name).orElseThrow().getId();
    }

    private User calculateUserActivity(User user) {
        int browsingCount = logService.getBrowsingLogs(user.getUsername()).size();
        UserActivity userActivity;

        int activityPoints = Math.max(0, user.getUserProfile().getActivityPoints() + browsingCount - MINIMUM_VISIT_COUNTS);

        if (activityPoints < 10)
            userActivity = UserActivity.LOW;
        else if (browsingCount < 20)
            userActivity = UserActivity.AVERAGE;
        else if (browsingCount < 30)
            userActivity = UserActivity.FREQUENT;
        else
            userActivity = UserActivity.HIGH;

        user.getUserProfile().setActivityPoints(activityPoints);
        user.getUserProfile().setActivity(userActivity);

        return user;
    }

    private CategoryWords getCategoryFromSavedUser(String categoryName, User user) {
        return userRepository.save(user)
                .getUserProfile()
                .getCategoriesWithWords()
                .stream()
                .filter(x -> x.getName().equals(categoryName))
                .findAny()
                .orElseThrow();
    }
}

