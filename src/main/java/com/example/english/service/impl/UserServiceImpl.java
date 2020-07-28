package com.example.english.service.impl;

import com.example.english.data.entity.*;
import com.example.english.data.model.binding.CategoryWordsBindingModel;
import com.example.english.data.model.service.CategoryWordsServiceModel;
import com.example.english.data.model.service.UserProfileServiceModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.data.model.service.WordServiceModel;
import com.example.english.data.repository.UserRepository;
import com.example.english.service.CategoryWordsService;
import com.example.english.service.RoleService;
import com.example.english.service.UserService;
import com.example.english.service.WordService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final CategoryWordsService categoryWordsService;
    private final WordService wordService;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }

    @Override
    public Optional<User> register(UserServiceModel userServiceModel) throws IllegalArgumentException {

        if (userRepository.findByUsername(userServiceModel.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Such username already exists!");
        }

        if (userRepository.findByEmail(userServiceModel.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Such email already exists!");
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

        return Optional.of(userRepository.save(user));
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
        User user = userRepository.findById(id)
                .orElseThrow();

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
        return modelMapper.map(
                userRepository.findById(id).orElseThrow(),
                UserServiceModel.class);
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
        User user = userRepository.findByUsername(userProfileServiceModel.getUsername()).orElseThrow();
        UserProfile map = modelMapper.map(userProfileServiceModel, UserProfile.class);
        user.setUserProfile(map);
        return modelMapper.map(userRepository.save(user).getUserProfile(),
                UserProfileServiceModel.class);
    }

    @Override
    public void deleteProfile(String id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUserProfile(null);
        //da sa vidi
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

        Set<CategoryWords> oldCategoryWords = user.getUserProfile().getCategoryWords();
        user.setUserProfile(modelMapper.map(userProfileServiceModel, UserProfile.class));
        user.getUserProfile().setCategoryWords(oldCategoryWords);
        User save = userRepository.save(user);

        return modelMapper.map(save.getUserProfile(), UserProfileServiceModel.class)
                .setUsername(user.getUsername())
                .setEmail(user.getEmail());
    }

    @Override
    public UserProfileServiceModel getUserProfileById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow();

        UserProfileServiceModel map = modelMapper.map(user.getUserProfile(), UserProfileServiceModel.class);
        return map.setUsername(user.getUsername()).setEmail(user.getEmail());
    }

    @Override
    public CategoryWordsServiceModel addCategoryForUser(String userId, CategoryWordsBindingModel categoryName) {
        CategoryWordsServiceModel serviceModel = categoryWordsService.addCategory(categoryName.getName());
        CategoryWords map = modelMapper.map(serviceModel, CategoryWords.class);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No such user"));

        Set<CategoryWords> categoryWords = user.getUserProfile().getCategoryWords();
        categoryWords.add(map);
        userRepository.saveAndFlush(user);

        return modelMapper.map(map, CategoryWordsServiceModel.class);
    }

    @Override
    public Set<CategoryWordsServiceModel> getUserCategoriesById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow()
                .getUserProfile()
                .getCategoryWords()
                .stream()
                .map(x -> modelMapper.map(x, CategoryWordsServiceModel.class))
                .collect(Collectors.toSet());
    }

    @Override
    public WordServiceModel addWordToUserCategoryWords(WordServiceModel wordServiceModel, String categoryId, String userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Word map1 = modelMapper.map(wordService.createWord(wordServiceModel), Word.class);

        CategoryWords categoryWords = user.getUserProfile()
                .getCategoryWords()
                .stream()
                .filter(c -> c.getId().equals(categoryId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No such category"));

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
    public Collection<CategoryWordsServiceModel> getWordsCategoryById(String id) {
        return userRepository
                .findById(id)
                .orElseThrow()
                .getUserProfile()
                .getCategoryWords()
                .stream()
                .map(x -> modelMapper.map(x, CategoryWordsServiceModel.class))
                .collect(Collectors.toList());
    }
}
