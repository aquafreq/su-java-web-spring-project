package com.example.english.controller;

import com.example.english.BaseControllerTest;
import com.example.english.annotations.WithMockedAdmin;
import com.example.english.data.entity.CategoryWords;
import com.example.english.data.entity.User;
import com.example.english.data.entity.enumerations.LevelExperience;
import com.example.english.data.entity.enumerations.LevelOfLanguage;
import com.example.english.data.model.binding.CategoryWordsBindingModel;
import com.example.english.data.model.binding.UserPasswordBindingModel;
import com.example.english.data.model.binding.UserProfileBindingModel;
import com.example.english.data.model.binding.WordBindingModel;
import com.example.english.data.repository.CategoryWordsRepository;
import com.example.english.data.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.example.english.constants.CategoryConstants.CATEGORY_NAME_MUST_BE_MORE_THAN_2_CHARACTERS;
import static com.example.english.constants.UserConstants.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTests extends BaseControllerTest {
    public static final String NO_VALUE_PRESENT = "No value present";
    public static final String ACCESS_IS_DENIED = "Access is denied";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository repository;

    @Autowired
    CategoryWordsRepository categoryWordsRepository;

    User fizz;
    CategoryWords categoryWords;

    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void init() {
        passwordEncoder = new Argon2PasswordEncoder();
        fizz = repository.findByUsername("fizz").orElse(null);
        assert fizz != null;
        categoryWords = fizz.getUserProfile().getCategoriesWithWords().stream()
                .filter(c -> c.getName().equals("IT terminology")).findAny().orElse(null);
    }

    @WithMockedAdmin
    @Test
    public void addWordToCategory_whenAuthAndUserValidAndWordValid_shouldAddAndReturnWord() throws Exception {
        WordBindingModel build = WordBindingModel.builder().name("zxczxc").definition("zxczxcdef").build();

        String path = String.format("/user/profile/%s/%s/create-word", fizz.getId(), categoryWords.getId());

        String s = new ObjectMapper().writeValueAsString(build);

        mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(s))
                .andExpect(status().isCreated());
    }

    @WithMockedAdmin
    @Test
    public void addWordToCategory_whenAuthAndUserValidAndWordValidWithExistingWord_shouldReturnExistingError() throws Exception {
        WordBindingModel build = WordBindingModel.builder().name("zxczxc").definition("zxczxcdef").build();
        String path = String.format("/user/profile/%s/%s/create-word", fizz.getId(), categoryWords.getId());
        String s = new ObjectMapper().writeValueAsString(build);

        mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(s))
                .andExpect(status().isCreated());

        mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(s))
                .andExpect(status().isOk())
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("The word exists in this category already!")));
    }

    @Test
    public void addWordToCategory_whenNoAuthAndUserValidAndWordValid_shouldReturnAccessDeniedError() throws Exception {
        WordBindingModel build = WordBindingModel.builder().name("zxczxc").definition("zxczxcdef").build();
        String user = repository.findByUsername("Bai ИВАН").get().getId();
        String path = String.format("/user/profile/%s/%s/create-word", user, categoryWords.getId());
        String s = new ObjectMapper().writeValueAsString(build);

        mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(s))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(401)))
                .andExpect(jsonPath("$.message", is(ACCESS_IS_DENIED)));
    }

    @WithMockedAdmin
    @Test
    public void addWordToCategory_whenAuthAndUserInvalidAndWordValid_shouldReturnNoValueError() throws Exception {
        WordBindingModel build = WordBindingModel.builder().name("zxczxc").definition("zxczxcdef").build();
        String path = String.format("/user/profile/%s/%s/create-word", "invalidUserLol", categoryWords.getId());
        String s = new ObjectMapper().writeValueAsString(build);

        mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(s))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is(NO_VALUE_PRESENT)));
    }

    @WithMockedAdmin
    @Test
    public void addWordToCategory_whenAuthAndUserValidAndValidWordAndInvalidCategory_shouldReturnNoValueError() throws Exception {
        WordBindingModel build = WordBindingModel.builder().name("daf").definition("dafdaf").build();
        String path = String.format("/user/profile/%s/%s/create-word", fizz.getId(), "dwafw");
        String s = new ObjectMapper().writeValueAsString(build);

        mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(s))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("No such category")));
    }

    @WithMockedAdmin
    @Test
    public void createWordCategory_whenAllValid_shouldCreateCategory() throws Exception {
        CategoryWordsBindingModel categoryWordsBindingModel = new CategoryWordsBindingModel();
        categoryWordsBindingModel.setName("category");
        String s = new ObjectMapper().writeValueAsString(categoryWordsBindingModel);

        mockMvc.perform(post("/user/profile/" + fizz.getId() + "/create-category")
                .contentType("application/json")
                .content(s))
                .andExpect(status().isCreated());
    }


    @Test
    public void createWordCategory_whenNoAuth_shouldReturnAccessDeniedError() throws Exception {
        CategoryWordsBindingModel categoryWordsBindingModel = new CategoryWordsBindingModel();
        categoryWordsBindingModel.setName("category");
        String s = new ObjectMapper().writeValueAsString(categoryWordsBindingModel);

        mockMvc.perform(post("/user/profile/" + fizz.getId() + "/create-category")
                .contentType("application/json")
                .content(s))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(401)))
                .andExpect(jsonPath("$.message", is(ACCESS_IS_DENIED)));
    }

    @WithMockedAdmin
    @Test
    public void createWordCategory_whenInvalidUser_shouldReturnNoValueError() throws Exception {
        CategoryWordsBindingModel categoryWordsBindingModel = new CategoryWordsBindingModel();
        categoryWordsBindingModel.setName("category");
        String s = new ObjectMapper().writeValueAsString(categoryWordsBindingModel);

        mockMvc.perform(post("/user/profile/" + "+fizz.getId()+" + "/create-category")
                .contentType("application/json")
                .content(s))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("No such user")));
    }

    @WithMockedAdmin
    @Test
    public void createWordCategory_whenExistingCategory_shouldReturnAlreadyExistsError() throws Exception {
        CategoryWordsBindingModel categoryWordsBindingModel = new CategoryWordsBindingModel();
        categoryWordsBindingModel.setName("category");
        String s = new ObjectMapper().writeValueAsString(categoryWordsBindingModel);

        mockMvc.perform(post("/user/profile/" + fizz.getId() + "/create-category")
                .contentType("application/json")
                .content(s));

        mockMvc.perform(post("/user/profile/" + fizz.getId() + "/create-category")
                .contentType("application/json")
                .content(s))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("category already exists, add another one or delete this one!")));
    }

    @WithMockedAdmin
    @Test
    public void createWordCategory_whenInvalidCategory_shouldReturnError() throws Exception {
        CategoryWordsBindingModel categoryWordsBindingModel = new CategoryWordsBindingModel();
        categoryWordsBindingModel.setName("q");
        String s = new ObjectMapper().writeValueAsString(categoryWordsBindingModel);

        MvcResult mvcResult = mockMvc.perform(post("/user/profile/" + fizz.getId() + "/create-category")
                .contentType("application/json")
                .content(s))
                .andExpect(MvcResult::getResolvedException)
                .andReturn();

        Assertions.assertEquals(400, mvcResult.getResponse().getStatus());
        Assertions.assertEquals("name: " + CATEGORY_NAME_MUST_BE_MORE_THAN_2_CHARACTERS,
                mvcResult.getResolvedException().getMessage());
    }

    @WithMockedAdmin
    @Test
    public void createWordCategory_whenNullCategory_shouldReturnError() throws Exception {
        CategoryWordsBindingModel categoryWordsBindingModel = new CategoryWordsBindingModel();
        String s = new ObjectMapper().writeValueAsString(categoryWordsBindingModel);

        MvcResult mvcResult = mockMvc.perform(post("/user/profile/" + fizz.getId() + "/create-category")
                .contentType("application/json")
                .content(s))
                .andExpect(MvcResult::getResolvedException)
                .andReturn();

        Assertions.assertEquals(400, mvcResult.getResponse().getStatus());
        Assertions.assertEquals("name: Name for category is required!", mvcResult.getResolvedException().getMessage());
    }

    @WithMockedAdmin
    @Test
    public void getUserProfile_whenValidIdAndValidAuth_shouldReturnProfile() throws Exception {
        mockMvc.perform(get("/user/profile/" + fizz.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(fizz.getUsername())))
                .andExpect(jsonPath("$.email", is(fizz.getEmail())))
                .andExpect(jsonPath("$.activity", is(fizz.getUserProfile().getActivity().name())));
    }

    @Test
    public void getUserProfile_whenValidIdAndNoAuth_shouldReturnAccessDenied() throws Exception {
        mockMvc.perform(get("/user/profile/" + fizz.getId()))
                .andExpect(status().isOk())
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(401)))
                .andExpect(jsonPath("$.message", is(ACCESS_IS_DENIED)));
    }

    @WithMockedAdmin
    @Test
    public void getUserProfile_whenInvalidIdAndValidAuth_shouldReturnNoValueError() throws Exception {
        mockMvc.perform(get("/user/profile/" + "fizz.getId())"))
                .andExpect(status().isOk())
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is(NO_VALUE_PRESENT)));
    }

    @WithMockedAdmin
    @Test
    public void updateUserProfile_whenValidUserAndProfileWithAuth_shouldUpdate() throws Exception {
        UserProfileBindingModel build = buildProfile();
        String s = new ObjectMapper().writeValueAsString(build);

        mockMvc.perform(patch("/user/profile/" + fizz.getId() + "/edit")
                .contentType(MediaType.APPLICATION_JSON).content(s))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(build.getUsername())))
                .andExpect(jsonPath("$.email", is(build.getEmail())))
                .andExpect(jsonPath("$.nationality", is(build.getNationality())))
                .andExpect(jsonPath("$.levelOfLanguage", is(build.getLevelOfLanguage())))
                .andExpect(jsonPath("$.levelExperience", is(build.getLevelExperience())))
                .andExpect(jsonPath("$.hobbies", hasSize(3)));
    }

    private UserProfileBindingModel buildProfile() {
        return UserProfileBindingModel
                .builder().email("fizzkata@abv.bg").username("fizzkata")
                .hobbies("zxc, qwe, rty").levelExperience(LevelExperience.ADVANCED.name())
                .levelOfLanguage(LevelOfLanguage.C2.name()).nationality("Afrikan").build();
    }

    @Test
    public void updateUserProfile_whenValidUserAndProfileWithoutAuth_shouldReturnAccessDeniedError() throws Exception {
        UserProfileBindingModel build = buildProfile();
        String s = new ObjectMapper().writeValueAsString(build);

        mockMvc.perform(patch("/user/profile/" + fizz.getId() + "/edit")
                .contentType(MediaType.APPLICATION_JSON).content(s))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(ACCESS_IS_DENIED)))
                .andExpect(jsonPath("$.statusCode", is(401)));
    }

    @WithMockedAdmin
    @Test
    public void updateUserProfile_whenInvalidUserAndValidProfileWithAuth_shouldReturnNoValueFoundError() throws Exception {
        UserProfileBindingModel build = buildProfile();
        String s = new ObjectMapper().writeValueAsString(build);

        mockMvc.perform(patch("/user/profile/" + "fizz.getId()" + "/edit")
                .contentType(MediaType.APPLICATION_JSON).content(s))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(NO_VALUE_PRESENT)))
                .andExpect(jsonPath("$.statusCode", is(404)));
    }

    @WithMockedAdmin
    @Test
    public void updateUserProfile_whenValidUserWithInvalidProfileDataWithAuth_shouldReturnConstraintError() throws Exception {
        UserProfileBindingModel userProfileBindingModel = new UserProfileBindingModel();
        String s = new ObjectMapper().writeValueAsString(userProfileBindingModel);

        mockMvc.perform(patch("/user/profile/" + fizz.getId() + "/edit")
                .contentType(MediaType.APPLICATION_JSON).content(s))
                .andExpect(status().isBadRequest())
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("email[0]", equalTo(EMAIL_IS_REQUIRED)))
                .andExpect(jsonPath("username[0]", equalTo(USERNAME_IS_REQUIRED)));

        UserProfileBindingModel build = UserProfileBindingModel.builder().username("z").email("wow").build();
        String s2 = new ObjectMapper().writeValueAsString(build);

        mockMvc.perform(patch("/user/profile/" + fizz.getId() + "/edit")
                .contentType(MediaType.APPLICATION_JSON).content(s2))
                .andExpect(status().isBadRequest())
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("email[0]", equalTo(EMAIL_MUST_BE_VALID)))
                .andExpect(jsonPath("username[0]", equalTo(USERNAME_MUST_BE_BETWEEN_30_CHARACTERS)));
    }

    @WithMockedAdmin
    @Test
    public void changeUserPassword_whenValidUserWithAuth_shouldReturnTrueAndChangePassword() throws Exception {
        UserPasswordBindingModel userPasswordBindingModel = new UserPasswordBindingModel();
        userPasswordBindingModel.setNewPassword("wow");
        userPasswordBindingModel.setOldPassword("fizz");

        String s = new ObjectMapper().writeValueAsString(userPasswordBindingModel);
        MvcResult mvcResult = mockMvc.perform(patch("/user/profile/" + fizz.getId() + "/change-password")
                .contentType("application/json")
                .content(s))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("true", mvcResult.getResponse().getContentAsString());
    }

    @WithMockedAdmin
    @Test
    public void changeUserPassword_whenInvalidUserWithAuth_shouldReturnNoValueFoundError() throws Exception {
        UserPasswordBindingModel userPasswordBindingModel = new UserPasswordBindingModel();
        userPasswordBindingModel.setNewPassword("wow");
        userPasswordBindingModel.setOldPassword("fizz");

        String s = new ObjectMapper().writeValueAsString(userPasswordBindingModel);
        MvcResult mvcResult = mockMvc.perform(patch("/user/profile/" + "fizz.getId()" + "/change-password")
                .contentType("application/json").content(s))
                .andExpect(status().isOk())
                .andReturn();
    }

    @WithMockedAdmin
    @Test
    public void changeUserPassword_whenValidUserWithAuthButWrongPassword_shouldReturnFalse() throws Exception {
        UserPasswordBindingModel userPasswordBindingModel = new UserPasswordBindingModel();
        userPasswordBindingModel.setNewPassword("wow");
        userPasswordBindingModel.setOldPassword("fizzz");

        String s = new ObjectMapper().writeValueAsString(userPasswordBindingModel);
        MvcResult mvcResult = mockMvc.perform(patch("/user/profile/" + fizz.getId() + "/change-password")
                .contentType("application/json").content(s))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("false", mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void changeUserPassword_whenValidUserWithNoAuth_shouldReturnAccessDeniedError() throws Exception {
        UserPasswordBindingModel userPasswordBindingModel = new UserPasswordBindingModel();
        userPasswordBindingModel.setNewPassword("wow");
        userPasswordBindingModel.setOldPassword("fizz");

        String s = new ObjectMapper().writeValueAsString(userPasswordBindingModel);
        mockMvc.perform(patch("/user/profile/" + fizz.getId() + "/change-password")
                .contentType("application/json").content(s))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", is(401)))
                .andExpect(jsonPath("$.message", is(ACCESS_IS_DENIED)));
    }

    @WithMockedAdmin
    @Test
    public void getUserCategoryWords_whenValidUserWithAuth_shouldReturnUserCategoryWords() throws Exception {
        mockMvc.perform(get("/user/profile/"+ fizz.getId()+"/practice"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[?(@.name =~ /^IT terminology$/)]", notNullValue()))
//                .andExpect(jsonPath("$[?(@.name =~ /^IT terminology$/)].words.length",is(5)))
                .andExpect(jsonPath("$[?(@.name =~ /^Unit 10 from C1 Advanced Face2Face book$/)]", notNullValue()));
//                .andExpect(jsonPath("$[?(@.name == 'Unit 10 from C1 Advanced Face2Face book')].words.length",is(6)));
    }

    @WithMockedAdmin
    @Test
    public void getUserCategoryWords_whenInvalidUserWithAuth_shouldReturnNoValueFoundError() throws Exception {
        mockMvc.perform(get("/user/profile/"+ "fizz.getId()"+"/practice"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is(NO_VALUE_PRESENT)));
    }

    @Test
    public void getUserCategoryWords_whenValidUserWithNoAuth_shouldReturnAccessDeniedError() throws Exception {
        mockMvc.perform(get("/user/profile/"+ fizz.getId()+"/practice"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(401)))
                .andExpect(jsonPath("$.message", is(ACCESS_IS_DENIED)));
    }
}