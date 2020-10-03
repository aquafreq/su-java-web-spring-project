package com.example.english.service;

import com.example.english.BaseTest;
import com.example.english.data.entity.CategoryWords;
import com.example.english.data.entity.Role;
import com.example.english.data.entity.User;
import com.example.english.data.entity.Word;
import com.example.english.data.entity.enumerations.LevelExperience;
import com.example.english.data.entity.enumerations.LevelOfLanguage;
import com.example.english.data.entity.enumerations.RoleEnum;
import com.example.english.data.model.service.CategoryWordsServiceModel;
import com.example.english.data.model.service.UserProfileServiceModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.data.model.service.WordServiceModel;
import com.example.english.data.repository.CategoryWordsRepository;
import com.example.english.data.repository.RoleRepository;
import com.example.english.data.repository.UserRepository;
import com.example.english.data.repository.WordRepository;
import com.example.english.service.impl.RoleServiceImpl;
import com.example.english.service.impl.UserServiceImpl;
import com.example.english.service.impl.WordServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests extends BaseTest {
    public static final String USERNAME = "zxc";
    public static final String PASSWORD = USERNAME;
    public static final String EMAIL = "zxc@zxc.zxc";
    UserService service;

    @Autowired
    UserRepository repository;

    RoleService roleService;

    @Autowired
    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;

    @MockBean
    CategoryWordsService categoryWordsService;

    @Mock
    WordServiceImpl wordService;

    ModelMapper modelMapper;

    @Autowired
    CategoryWordsRepository categoryWordsRepository;

    User user;

    @Autowired
    WordRepository wordRepository;
    LogService logService;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        passwordEncoder = new Argon2PasswordEncoder();

        MockitoAnnotations.initMocks(this);
        roleService = new RoleServiceImpl(modelMapper, roleRepository);

        service = new UserServiceImpl(
                repository,
                roleService,
                passwordEncoder,
                categoryWordsService,
                wordService,
                modelMapper,
                logService
        );

        user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setEmail(EMAIL);
    }

    @Test
    public void loadUserByUsername_whenValidUsername_shouldReturnUserDetails() {
        User save = repository.save(user);
        UserDetails userDetails = service.loadUserByUsername(save.getUsername());

        assertEquals(USERNAME, userDetails.getUsername());
        assertEquals(PASSWORD, userDetails.getPassword());
    }

    @Test
    public void loadUserByUsername_whenInvalidUsername_shouldThrow() {
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("save.getUsername()"));
    }

    @Test
    public void register_withTwoValidUsers_shouldReturnAdminAndUser() {
        User user1 = new User();
        user1.setUsername("bai_ivan");
        user1.setPassword("bai_ivan");
        user1.setEmail("bai_ivan@bai_ivan.bai_ivan");

        seedRoles();

        UserServiceModel map = modelMapper.map(user, UserServiceModel.class);
        UserServiceModel ordinaryUser = modelMapper.map(user1, UserServiceModel.class);

        User admin = service.register(map).get();
        User noAdminUser = service.register(ordinaryUser).get();

        assertEquals(USERNAME, admin.getUsername());
        assertTrue(passwordEncoder.matches(user.getPassword(), admin.getPassword()));
        assertEquals(EMAIL, admin.getEmail());
        assertEquals(4, admin.getAuthorities().size());

        assertEquals(user1.getUsername(), noAdminUser.getUsername());
        assertTrue(passwordEncoder.matches(user1.getPassword(), noAdminUser.getPassword()));
        assertEquals(user1.getEmail(), noAdminUser.getEmail());
        assertEquals(1, noAdminUser.getAuthorities().size());
    }

    private void seedRoles() {
        roleService.seedRoles(
                Arrays.stream(RoleEnum.values())
                        .map(r -> new Role(r.name())).collect(Collectors.toList()));
    }

    @Test
    public void register_whenDuplicateUsername_shouldThrow() {
        UserServiceModel map = modelMapper.map(user, UserServiceModel.class);
        service.register(map);

        assertThrows(IllegalArgumentException.class, () -> service.register(map));
    }

    @Test
    public void register_whenDuplicateEmail_shouldThrow() {
        UserServiceModel map = modelMapper.map(user, UserServiceModel.class);
        map.setUsername("changed");
        service.register(map);

        assertThrows(IllegalArgumentException.class, () -> service.register(map));
    }

    @Test
    public void getCount_whenHasUsers_shouldReturnCount() {
        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername("qqq");
        userServiceModel.setPassword("qqq");
        userServiceModel.setEmail("qqq@qqq.qqq");

        UserServiceModel userServiceModel1 = new UserServiceModel();
        userServiceModel1.setUsername("www");
        userServiceModel1.setPassword("www");
        userServiceModel1.setEmail("www@www.www");

        service.register(userServiceModel);
        service.register(userServiceModel1);

        assertEquals(2, service.getCount());
    }

    //to check for user throw
    @Test
    public void giveUserRole_whenValidUser_shouldIncreaseSizeAndAddsRole() {
        seedRoles();
        Role role_admin = roleService.getRoleByName("ROLE_ADMIN");

        User save = repository.save(user);
        UserServiceModel userServiceModel = service.giveUserRole(save.getId(), role_admin.getId());

        assertEquals(1, userServiceModel.getAuthorities().size());
        assertTrue(userServiceModel
                .getAuthorities()
                .stream()
                .anyMatch(r -> r.getAuthority().equals(role_admin.getAuthority())));
    }

    @Test
    public void giveUserRole_whenInvalidUser_shouldThrow() {
        seedRoles();
        Role role_admin = roleService.getRoleByName("ROLE_ADMIN");

        assertThrows(NoSuchElementException.class,
                () -> service.giveUserRole("save.getId()", role_admin.getId()));
    }

    @Test
    public void removeUserRole_whenValidUser_shouldDecreaseSizeAndRemoveRole() {
        seedRoles();
        User register = service.register(modelMapper.map(user, UserServiceModel.class)).get();
        Role role_root_admin = roleService.getRoleByName("ROLE_ROOT_ADMIN");

        UserServiceModel userServiceModel =
                service.removeUserRole(register.getId(), role_root_admin.getId());

        assertEquals(3, userServiceModel.getAuthorities().size());
        assertTrue(
                userServiceModel
                        .getAuthorities()
                        .stream()
                        .noneMatch(r -> r.getAuthority().equals(role_root_admin.getAuthority()))
        );
    }

    @Test
    public void removeUserRole_whenInvalidUser_shouldThrow() {
        assertThrows(NoSuchElementException.class,
                () -> service.removeUserRole("zzzz", "wwww"));
    }

    @Test
    public void forbidUser_whenValidUserId_shouldDisableUser() {
        User register = service.register(modelMapper.map(user, UserServiceModel.class)).get();
        UserServiceModel userServiceModel = service.forbidUser(register.getId());
        assertFalse(userServiceModel.isEnabled());
    }

    @Test
    public void permitUser_whenValidUserId_shouldEnableUser() {
        User save = repository.save(user);
        UserServiceModel map = modelMapper.map(save, UserServiceModel.class);

        UserServiceModel userServiceModel = service.permitUser(map.getId());
        assertTrue(userServiceModel.isEnabled());
    }

    @Test
    public void forbidPermitUser_whenInvalidUserId_shouldThrow() {
        assertThrows(NoSuchElementException.class, () ->
                service.forbidUser("register.getId()"));
        assertThrows(NoSuchElementException.class, () ->
                service.permitUser("register.getId()"));
    }


    @Test
    public void getUserByName_whenValidName_shouldReturnUser() {
        repository.save(user);
        UserServiceModel userByName = service.getUserByName(USERNAME);
        assertEquals(USERNAME, userByName.getUsername());
        assertEquals(EMAIL, userByName.getEmail());
    }

    @Test
    public void getUserByName_whenInvalid_shouldThrow() {
        assertThrows(NoSuchElementException.class,
                () -> service.getUserByName(USERNAME));
    }

    @Test
    public void updateUser_whenUserIsValid_shouldUpdate() {
        User save = repository.save(user);
        save.setUsername("updated");
        service.updateUser(save);

        assertDoesNotThrow(() -> repository.findByUsername("updated"));
    }


    @Test
    public void saveProfile_whenInvalidUser_shouldThrow() {
        assertThrows(NoSuchElementException.class,
                () -> service.saveProfile(new UserProfileServiceModel().setUsername("throw")));
    }

    @Test
    public void saveProfile_whenValidUserAndProfile_shouldUpdateUser() {
        User register = service.register(modelMapper.map(user, UserServiceModel.class)).get();
        LocalDate now = LocalDate.now();
        UserProfileServiceModel profileServiceModel = new UserProfileServiceModel();
        profileServiceModel.setNationality("BG");
        profileServiceModel.setBirthDate(now);
        profileServiceModel.setUsername(register.getUsername());
        profileServiceModel.setEmail(register.getEmail());
        profileServiceModel.setLevelOfLanguage(LevelOfLanguage.A1);

        UserProfileServiceModel updated = service.saveProfile(profileServiceModel);

        assertEquals(now, updated.getBirthDate());
        assertEquals("BG", updated.getNationality());
        assertEquals(USERNAME, updated.getUsername());
        assertEquals(EMAIL, updated.getEmail());
        assertEquals(LevelOfLanguage.A1, updated.getLevelOfLanguage());
        assertEquals(register.getUserProfile().getId(), updated.getId());
    }

    @Test
    public void deleteProfile_whenInvalidUser_shouldThrow() {
        assertThrows(NoSuchElementException.class,
                () -> service.deleteProfile(USERNAME));
    }

    @Test
    public void deleteProfile_whenValidUser_shouldReturnProfileNull() {
        User save = repository.save(user);
        service.deleteProfile(save.getId());
        assertNull(save.getUserProfile());
    }

    @Test
    public void updateProfile_whenInvalidUser_shouldThrow() {
        assertThrows(NoSuchElementException.class,
                () -> service.updateProfile(new UserProfileServiceModel(), "throw"));
    }

    @Test
    public void updateProfile_whenValidUser_shouldUpdateUserProfile() {
        User register = service.register(modelMapper.map(user, UserServiceModel.class)).get();
        LocalDate now = LocalDate.now();
        UserProfileServiceModel profileServiceModel = new UserProfileServiceModel();
        profileServiceModel.setNationality("BG");
        profileServiceModel.setBirthDate(now);
        profileServiceModel.setUsername(register.getUsername());
        profileServiceModel.setEmail(register.getEmail());
        profileServiceModel.setLevelOfLanguage(LevelOfLanguage.A1);

        UserProfileServiceModel updated = service.saveProfile(profileServiceModel);
        updated.setLevelOfLanguage(LevelOfLanguage.C2);
        updated.setNationality("LevelOfLanguage.C2");
        updated.setUsername("LevelOfLanguage.C2");
        updated.setEmail("LevelOfLanguage@C2.C2");
        updated.setHobbies(Arrays.asList("WebFlux", "HATEOAS", "AOP"));

        UserProfileServiceModel newProfile = service.updateProfile(updated, register.getId());

        assertAll(() -> {
            assertEquals(updated.getLevelOfLanguage(), newProfile.getLevelOfLanguage());
            assertEquals(updated.getEmail(), newProfile.getEmail());
            assertEquals(updated.getUsername(), newProfile.getUsername());
            assertEquals(updated.getNationality(), newProfile.getNationality());
        });

//        newProfile.getHobbies().add("wow");
        assertThat(updated.getHobbies())
                .containsExactlyInAnyOrderElementsOf(newProfile.getHobbies());
    }

    @Test
    public void getUserProfileById_whenValidId_shouldReturnProfile() {
        User register = service.register(modelMapper.map(user, UserServiceModel.class)).get();
        LocalDate now = LocalDate.now();
        UserProfileServiceModel profileServiceModel = new UserProfileServiceModel();
        profileServiceModel.setNationality("BG");
        profileServiceModel.setBirthDate(now);
        profileServiceModel.setUsername(register.getUsername());
        profileServiceModel.setEmail(register.getEmail());
        profileServiceModel.setLevelOfLanguage(LevelOfLanguage.A1);

        service.saveProfile(profileServiceModel);

        UserProfileServiceModel gottenProfile = service.getUserProfileByUserId(register.getId());

        assertAll(() -> {
            assertEquals(profileServiceModel.getUsername(), gottenProfile.getUsername());
            assertEquals(profileServiceModel.getEmail(), gottenProfile.getEmail());
            assertEquals(profileServiceModel.getNationality(), gottenProfile.getNationality());
            assertEquals(profileServiceModel.getBirthDate(), gottenProfile.getBirthDate());
            assertEquals(profileServiceModel.getLevelOfLanguage(), gottenProfile.getLevelOfLanguage());
        });
    }

    @Test
    public void getUserProfileById_whenInvalidId_shouldThrow() {
        assertThrows(NoSuchElementException.class,
                () -> service.getUserProfileByUserId("new UserProfileServiceModel()"));
    }

    @Test
    public void addCategoryForUser_whenInvalidUser_shouldThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addCategoryForUser("1", "1"));
    }

    @Test
    public void addCategoryForUser_whenValidUser_shouldAddCategory() {
        User save = repository.save(user);

        CategoryWordsServiceModel serviceModel1 = new CategoryWordsServiceModel();
        serviceModel1.setName("zxc");

        when(categoryWordsService.addCategory(anyString())).thenReturn(serviceModel1);

        CategoryWordsServiceModel serviceModel = service.addCategoryForUser(save.getId(), USERNAME);

        assertEquals(1, save.getUserProfile().getCategoriesWithWords().size());
        assertEquals(USERNAME, serviceModel.getName());
    }

    @Test
    public void getUserCategoriesById_whenInvalidUserId_shouldThrow() {
        assertThrows(NoSuchElementException.class,
                () -> service.getUserCategoriesById("throw"));
    }

    @Test
    public void getUserCategoriesById_whenValidUserId_shouldReturnCategories() {
        User save = repository.save(user);
        CategoryWords categoryWords = new CategoryWords();
        categoryWords.setName("cat");

        CategoryWords categoryWords2 = new CategoryWords();
        categoryWords.setName("cat2");

        List<CategoryWords> expected = categoryWordsRepository.saveAll(
                Arrays.asList(categoryWordsRepository.save(categoryWords2),
                        categoryWordsRepository.save(categoryWords)));

        expected.forEach(e -> user.getUserProfile().getCategoriesWithWords().add(e));

        User save1 = repository.save(save);

        Set<CategoryWordsServiceModel> userCategoriesById = service.getUserCategoriesById(save1.getId());

        Set<CategoryWordsServiceModel> collect =
                expected.stream().map(x -> modelMapper.map(x, CategoryWordsServiceModel.class))
                        .collect(Collectors.toSet());

        assertEquals(2, userCategoriesById.size());
        assertEquals(collect, userCategoriesById);
    }

    @Test
    public void addWordToUserCategoryWords_whenInvalidInput_shouldThrow() {
        assertThrows(NoSuchElementException.class,
                () -> service.addWordToUserCategoryWords(new WordServiceModel(), "1", "1"));
    }

    @Test
    public void addWordToUserCategoryWords_whenInvalidCategory_shouldThrow() {
        User save = repository.save(user);
        assertThrows(IllegalArgumentException.class,
                () -> service.addWordToUserCategoryWords(new WordServiceModel(), "1", save.getId()));
    }

    @Test
    public void addWordToUserCategoryWords_whenValid_shouldAddAndIncreaseSize() {
        CategoryWords categoryWords = new CategoryWords();
        categoryWords.setName("cats");
        CategoryWords save1 = categoryWordsRepository.save(categoryWords);

        user.getUserProfile().getCategoriesWithWords().add(save1);
        User save = repository.save(user);

        WordServiceModel word = new WordServiceModel();
        word.setName("aaaa");
        word.setDefinition("bbbb");

        when(wordService.createWord(any(WordServiceModel.class)))
                .thenReturn(word);

        WordServiceModel wordServiceModel = service.addWordToUserCategoryWords(new WordServiceModel(), save1.getId(), save.getId());

        CategoryWords categoryWords1 = save.getUserProfile()
                .getCategoriesWithWords()
                .stream()
                .filter(f -> f.getName().equals(categoryWords.getName()))
                .findAny()
                .orElseThrow();

        assertEquals(word.getName(), wordServiceModel.getName());
        assertEquals(word.getDefinition(), wordServiceModel.getDefinition());
        assertEquals(word.getName(), categoryWords1.getWords().get(0).getName());
        assertEquals(word.getDefinition(), categoryWords1.getWords().get(0).getDefinition());
        assertEquals(1, categoryWords1.getWords().size());
    }

    @Test
    public void updatePassword_whenInvalidUser_shouldThrow() {
        assertThrows(NoSuchElementException.class,
                () -> service.updatePassword("1", "1", "1"));
    }

    @Test
    public void updatePassword_whenValidUserAndPassword_shouldChange() {
        UserServiceModel map = modelMapper.map(user, UserServiceModel.class);
        User register = service.register(map).get();

        boolean newpass = service.updatePassword(register.getId(), user.getPassword(), "NEWPASS");
        assertTrue(newpass);
        assertTrue(passwordEncoder.matches("NEWPASS", register.getPassword()));
    }

    @Test
    public void updatePassword_whenValidUserAndInvalidPassword_shouldNotChange() {
        UserServiceModel map = modelMapper.map(user, UserServiceModel.class);
        User register = service.register(map).get();

        boolean newpass = service.updatePassword(register.getId(), "PASSWORD", "NEWPASS");
        assertFalse(newpass);
        assertTrue(passwordEncoder.matches(user.getPassword(), register.getPassword()));
    }

    @Test
    public void getUserDetailsById_whenInvalidId_shouldThrow() {
        assertThrows(NoSuchElementException.class,
                () -> service.getUserDetailsById("throw"));
    }

    @Test
    public void getUserDetailsById_whenValidId_shouldReturnUser() {
        LocalDate now = LocalDate.now();

        user.getUserProfile().setBirthDate(now);
        user.getUserProfile().setLevelExperience(LevelExperience.BEGINNER);
        user.getUserProfile().setNationality("LevelExperience.BEGINNER");

        User register = service.register(modelMapper.map(user, UserServiceModel.class)).get();

        UserServiceModel userDetailsById = service.getUserDetailsById(register.getId());
        assertEquals(user.getUserProfile().getBirthDate(), userDetailsById.getUserProfileBirthDate());
        assertEquals(user.getUserProfile().getLevelExperience(), userDetailsById.getUserProfileLevelExperience());
        assertEquals(user.getUserProfile().getNationality(), userDetailsById.getUserProfileNationality());
        assertEquals(user.getUsername(), userDetailsById.getUsername());
        assertEquals(user.getEmail(), userDetailsById.getEmail());
    }

    @Test
    public void getCategoriesWithWords_whenValidUserId_shouldReturnCategoriesWithWords() {
        User register = service.register(modelMapper.map(user, UserServiceModel.class)).get();

        CategoryWords categoryWords = new CategoryWords();
        categoryWords.setName("cats");

        CategoryWords categoryWords2 = new CategoryWords();
        categoryWords2.setName("cats2");

        CategoryWords categoryWords3 = new CategoryWords();
        categoryWords3.setName("cats3");

        CategoryWords save1 = categoryWordsRepository.save(categoryWords);
        CategoryWords save2 = categoryWordsRepository.save(categoryWords2);
        CategoryWords save3 = categoryWordsRepository.save(categoryWords3);

        ArrayList<CategoryWords> objects =
                new ArrayList<>() {{
                    add(save1);
                    add(save2);
                    add(save3);
                }};

        user.getUserProfile().getCategoriesWithWords().addAll(objects);

        User save = repository.save(user);

        WordServiceModel word = new WordServiceModel();
        word.setName("aaaa");
        word.setDefinition("bbbb");

        when(wordService.createWord(any(WordServiceModel.class))).thenReturn(word);

        service.addWordToUserCategoryWords(new WordServiceModel(), save1.getId(), save.getId());

        List<CategoryWordsServiceModel> expected =
                objects.stream().map(x -> modelMapper.map(x, CategoryWordsServiceModel.class))
                        .collect(Collectors.toList());

        List<CategoryWordsServiceModel> actual =
                new ArrayList<>(service.getWordsCategoryByUserId(save.getId()));

        assertEquals(3, actual.size());
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void deleteWordFromCategoryByUserId_whenValidInput_shouldDelete() {
        User register = service.register(modelMapper.map(user, UserServiceModel.class)).get();

        CategoryWords categoryWords = new CategoryWords();
        categoryWords.setName("wow");

        User save1 = repository.save(register);
        Word word = Word.builder().name("word1").definition("def1").build();
        Word word2 = Word.builder().name("word2").definition("def2").build();
        Word word3 = Word.builder().name("word3").definition("def3").build();

        categoryWords.setWords(Arrays.asList(word, word2, word3));
        CategoryWords save = categoryWordsRepository.save(categoryWords);

        register.getUserProfile().getCategoriesWithWords().add(save);

        doAnswer(invocation -> {
            Object[] arguments = invocation.getArguments();
            Set<CategoryWords> collect1 = register.getUserProfile().getCategoriesWithWords()
                    .stream()
                    .filter(x -> x.getId().equals(arguments[0]))
                    .filter(x -> {
                        List<Word> collect = x.getWords()
                                .stream()
                                .filter(w -> !w.getName().equals(arguments[1]))
                                .collect(Collectors.toList());

                        x.setWords(collect);
                        return true;
                    }).collect(Collectors.toSet());

            register.getUserProfile().setCategoriesWithWords(collect1);
            return null;
        }).when(categoryWordsService).removeWordFromCategory(anyString(), anyString());

        CategoryWordsServiceModel map = modelMapper.map(save, CategoryWordsServiceModel.class);
        map.setWordName("word2");

        CategoryWordsServiceModel serviceModel = service.deleteWordFromCategoryByUserId(
                save1.getId(), map);

        verify(categoryWordsService, times(1))
                .removeWordFromCategory(anyString(), anyString());

        assertEquals(2, serviceModel.getWords().size());
        assertEquals(categoryWords.getName(), serviceModel.getName());
        assertTrue(serviceModel.getWords().stream().noneMatch(w -> w.getName().equals("word2")));
    }

    @Test
    public void deleteWordFromCategoryByUserId_whenInvalidCategory_shouldThrow() {
        User save = repository.save(user);
        assertThrows(NoSuchElementException.class,
                () -> service.deleteWordFromCategoryByUserId(save.getId(), new CategoryWordsServiceModel()));
    }

    @Test
    public void deleteCategoryByUserIdAndCategoryName_whenValid_shouldDeleteCategory() {
        User map = modelMapper.map(service.register(modelMapper.map(user, UserServiceModel.class)).get(), User.class);

        CategoryWords categoryWords = new CategoryWords();
        categoryWords.setName("wow");
        CategoryWords save = categoryWordsRepository.save(categoryWords);

        map.getUserProfile().getCategoriesWithWords().add(categoryWords);

        assertEquals(1, map.getUserProfile().getCategoriesWithWords().size());

        doAnswer(invocation -> {
            String id = invocation.getArgument(0);
            map.getUserProfile()
                    .getCategoriesWithWords()
                    .removeIf(c -> c.getId().equals(id));
            return null;
        }).when(categoryWordsService).removeCategoryById(anyString());

        service.deleteCategoryByUserIdAndCategoryId(map.getId(), save.getId());

        assertEquals(0, map.getUserProfile().getCategoriesWithWords().size());
    }

    @Test
    public void getUserIdByUsername_whenValidId_shouldReturnUser(){
        User save = repository.save(user);
        String id = service.getUserIdByUsername(save.getUsername());
        assertEquals(save.getId(),id);
    }

    @Test
    public void getUserIdByUsername_whenInvalidId_shouldReturnNoValueError(){
        assertThrows(NoSuchElementException.class, () -> service.getUserByName("z"));
    }



}