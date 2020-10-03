package com.example.english.controller;

import com.example.english.BaseControllerTest;
import com.example.english.annotations.WithMockedAdmin;
import com.example.english.data.entity.BaseEntity;
import com.example.english.data.entity.Role;
import com.example.english.data.entity.User;
import com.example.english.data.repository.RoleRepository;
import com.example.english.data.repository.UserRepository;
import com.example.english.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.*
        ;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AdminUserControllerTests extends BaseControllerTest {
    public static final String NO_VALUE_PRESENT = "No value present";
    public static final String ACCESS_IS_DENIED = "Access is denied";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository repository;

    @Autowired
    RoleRepository roleRepository;

    //    PasswordEncoder passwordEncoder;
    ModelMapper modelMapper;
    User fizz;

    @BeforeEach
    public void init() {
        modelMapper = new ModelMapper();
        fizz = repository.findByUsername("fizz").get();
    }

    @Test
    public void getUser_whenValidUserWithNoAuth_shouldThrow() throws Exception {
        mockMvc.perform(get("/admin/user/1")).andExpect(MvcResult::getResolvedException);
    }

    @WithMockedAdmin
    @Test
    public void getUser_whenValidUserWithAuth_shouldReturnUser() throws Exception {
        mockMvc.perform(get("/admin/user/" + fizz.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("fizz")))
                .andExpect(jsonPath("$.id", is(fizz.getId())))
                .andExpect(jsonPath("$.authorities", hasSize(4)))
                .andExpect(jsonPath("$.email", is("fizz@fizz.fizz")));
    }

    @WithMockedAdmin
    @Test
    public void getUser_withInvalidUserAndValidAuth_shouldThrow() throws Exception {
        mockMvc.perform(get("/admin/user/" + "fizz.getId()"))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(404)));
    }

    @WithMockedAdmin
    @Test
    public void disableUser_whenValidUserAndValidAuth_shouldDisable() throws Exception {
        User user = repository.findByUsername("Bai ИВАН").get();

        mockMvc.perform(patch("/admin/user/disable/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.enabled", is(false)));
    }

    @WithMockedAdmin
    @Test
    public void enableUser_whenValidUserAndValidAuth_shouldEnable() throws Exception {
        User user = repository.findByUsername("Bai ИВАН").get();
        user.setEnabled(false);
        repository.save(user);

        mockMvc.perform(patch("/admin/user/permit/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.enabled", is(true)));
    }

    @WithMockedAdmin
    @Test
    public void enableUser_whenInvalidUserAndValidAuth_shouldThrow() throws Exception {
        mockMvc.perform(patch("/admin/user/permit/" + "user.getId()"))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is(NO_VALUE_PRESENT)));
    }

    @WithMockedAdmin
    @Test
    public void disableUser_whenInvalidUserAndValidAuth_shouldThrow() throws Exception {
        mockMvc.perform(patch("/admin/user/disable/" + "user.getId()"))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is(NO_VALUE_PRESENT)));
    }

    @Test
    public void disableUser_whenValidUserAndNoAuth_shouldThrow() throws Exception {
        User user = repository.findByUsername("fizz2").get();
        mockMvc.perform(patch("/admin/user/disable/" + user.getId()))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(401)))
                .andExpect(jsonPath("$.message", is(ACCESS_IS_DENIED)));
    }

    @Test
    public void enableUser_whenValidUserNoAuth_shouldThrow() throws Exception {
        User user = repository.findByUsername("fizz2").get();
        mockMvc.perform(patch("/admin/user/permit/" + user.getId()))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(401)))
                .andExpect(jsonPath("$.message", is(ACCESS_IS_DENIED)));
    }

    @WithMockedAdmin
    @Test
    public void getUserDetails_whenValidIdAndValidAuth_shouldReturnDetails() throws Exception {
        User user = repository.findByUsername("fizz").get();

        mockMvc.perform(get("/admin/user/details/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.userProfileActivity", is(user.getUserProfile().getActivity().name())));
    }

    @Test
    public void getUserDetails_whenValidIdAndNoAuth_shouldReturnAccessDeniedError() throws Exception {
        User user = repository.findByUsername("fizz").get();

        mockMvc.perform(get("/admin/user/details/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is((401))))
                .andExpect(jsonPath("$.message", is(ACCESS_IS_DENIED)));
    }

    @WithMockedAdmin
    @Test
    public void getUserDetails_whenInvalidIdAndValidAuth_shouldReturnNoResultError() throws Exception {
        mockMvc.perform(get("/admin/user/details/" + "user.getId()"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is((404))))
                .andExpect(jsonPath("$.message", is(NO_VALUE_PRESENT)));
    }

    @WithMockedAdmin
    @Test
    public void giveUserRole_whenValidUserAndAuth_shouldAddRoleToUser() throws Exception {
        String roleId = roleRepository.findByAuthority("ROLE_ADMIN").getId();
        User user = repository.findByUsername("Bai ИВАН").get();
        String url = String.format("/admin/user/%s/add-role/%s", user.getId(), roleId);

        mockMvc.perform(patch(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.username", is("Bai ИВАН")))
                .andExpect(jsonPath("$.authorities.length()", is(2)))
                .andExpect(jsonPath("$.authorities[?(@.authority == 'ROLE_ADMIN')].authority", hasItem("ROLE_ADMIN")));
//                .andExpect(jsonPath("$.authorities[?(@.authority.indexOf('ROLE_ADMIN') != -1)]",is(true)));
    }

    @WithMockedAdmin
    @Test
    public void giveUserRole_whenInvalidUserAndValidAuth_shouldReturnNoValueError() throws Exception {
        String roleId = roleRepository.findByAuthority("ROLE_ADMIN").getId();
        String url = String.format("/admin/user/1/add-role/%s", roleId);

        mockMvc.perform(patch(url))
                .andExpect(MvcResult::getResolvedException) //fixme
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is(NO_VALUE_PRESENT)));
    }

    @Test
    public void giveUserRole_whenValidUserAndNoAuth_shouldReturnNoAccessError() throws Exception {
        String roleId = roleRepository.findByAuthority("ROLE_ADMIN").getId();
        User user = repository.findByUsername("Bai ИВАН").get();
        String url = String.format("/admin/user/%s/add-role/%s", user.getId(), roleId);

        mockMvc.perform(patch(url))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(401)))
                .andExpect(jsonPath("$.message", is(ACCESS_IS_DENIED)));
    }

    //
    @WithMockedAdmin
    @Test
    public void removeUserRole_whenValidUserAndAuth_shouldRemoveUserRole() throws Exception {
        String roleId = roleRepository.findByAuthority("ROLE_MODERATOR").getId();
        User user = repository.findByUsername("wow_moderator").get();
        String url = String.format("/admin/user/%s/remove-role/%s", user.getId(), roleId);

        mockMvc.perform(patch(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.username", is("wow_moderator")))
                .andExpect(jsonPath("$.authorities.length()", is(2)))
                .andExpect(jsonPath("$.authorities[?(@.authority == 'ROLE_MODERATOR')].authority", empty()));
//                .andExpect(jsonPath("$.authorities[?(@.authority.indexOf('ROLE_ADMIN') != -1)]",is(true)));
    }

    @WithMockedAdmin
    @Test
    public void removeUserRole_whenInvalidUserAndValidAuth_shouldReturnNoValueError() throws Exception {
        String roleId = roleRepository.findByAuthority("ROLE_ADMIN").getId();
        String url = String.format("/admin/user/1/remove-role/%s", roleId);

        mockMvc.perform(patch(url))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is(NO_VALUE_PRESENT)));
    }

    @Test
    public void removeUserRole_whenValidUserAndNoAuth_shouldReturnNoAccessError() throws Exception {
        String roleId = roleRepository.findByAuthority("ROLE_ADMIN").getId();
        User user = repository.findByUsername("Bai ИВАН").get();
        String url = String.format("/admin/user/%s/remove-role/%s", user.getId(), roleId);

        mockMvc.perform(patch(url))
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(401)))
                .andExpect(jsonPath("$.message", is(ACCESS_IS_DENIED)));
    }

    @WithMockedAdmin
    @Test
    public void getUserRoles_whenAuth_shouldReturnRoles() throws Exception {
        List<Role> all = roleRepository.findAll();

        MvcResult mvcResult = mockMvc.perform(get("/admin/role/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(4)))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        List<Role> myObjects = new ObjectMapper().readValue(contentAsString, new TypeReference<>() {
        });

        Comparator<Role> objectComparator = Comparator.comparing(BaseEntity::getId);

        all.sort(objectComparator);
        myObjects.sort(objectComparator);

        for (int i = 0; i < all.size(); i++) {
            Role role = all.get(i);
            Role role1 = myObjects.get(i);
            assertEquals(role.getAuthority(), role1.getAuthority());
            assertEquals(role.getId(), role1.getId());
        }
    }

    @Test
    public void getUserRoles_whenNoAuth_shouldReturnError() throws Exception {
        mockMvc.perform(get("/admin/role/all"))
                .andExpect(status().isOk())
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(401))) //fixme
                .andExpect(jsonPath("$.message", is(ACCESS_IS_DENIED)));
    }
}
