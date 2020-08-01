package com.example.english.service;

import com.example.english.BaseTest;
import com.example.english.data.entity.User;
import com.example.english.data.model.service.CommentServiceModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.data.repository.CommentRepository;
import com.example.english.data.repository.UserRepository;
import com.example.english.service.impl.CommentServiceImpl;
import com.example.english.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CommentServiceTests extends BaseTest {
    // test coverage shows 100% on method and line,
    // with only one of the tests

    @Autowired
    CommentRepository commentRepository;

    ModelMapper modelMapper;

    UserService userService;

    CommentService service;

    @Autowired
    UserRepository userRepository;

    @MockBean
    RoleService roleService;
    @MockBean
    PasswordEncoder passwordEncoder;
    @MockBean
    CategoryWordsService categoryWordsService;
    @MockBean
    WordService wordService;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        userService = new UserServiceImpl(userRepository,
                roleService,
                passwordEncoder,
                categoryWordsService,
                wordService,
                modelMapper);

        service = new CommentServiceImpl(commentRepository, modelMapper, userService);
    }

    @Test
    public void addComment_whenAnonymousUser_shouldReturnNullUser() {
        CommentServiceModel model = new CommentServiceModel();
        model.setMessage("wow");
        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setId("Anonymous");
        model.setUserServiceModel(userServiceModel);

        CommentServiceModel addComment = service
                .addComment(model);

        assertEquals("wow", addComment.getMessage());
        assertNull(addComment.getUserServiceModel());
    }

    @Test
    public void addComment_whenHasUser_shouldReturnUser() {
        CommentServiceModel model = new CommentServiceModel();

        model.setMessage("wow");

        User user = new User();
        user.setUsername("zxc");
        user.setEmail("zxc@zxc.zxc");
        user.setPassword("zxc@zxc.zxc");
        User save = userRepository.save(user);
        UserServiceModel map = modelMapper.map(save, UserServiceModel.class);

        model.setUserServiceModel(map);

        CommentServiceModel addComment = service.addComment(model);

        assertEquals(model.getMessage(), addComment.getMessage());
        assertEquals(user.getUsername(), addComment.getUserServiceModel().getUsername());
    }
}