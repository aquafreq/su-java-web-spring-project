package com.example.english.service;

import com.example.english.BaseTest;
import com.example.english.data.entity.Comment;
import com.example.english.data.entity.Content;
import com.example.english.data.entity.GrammarCategory;
import com.example.english.data.entity.User;
import com.example.english.data.model.service.CommentServiceModel;
import com.example.english.data.model.service.ContentServiceModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.data.repository.CommentRepository;
import com.example.english.data.repository.ContentRepository;
import com.example.english.data.repository.GrammarCategoryRepository;
import com.example.english.data.repository.UserRepository;
import com.example.english.exceptions.NoContentFound;
import com.example.english.service.impl.ContentServiceImpl;
import com.example.english.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ContentServiceTests extends BaseTest {
    public static final String TITLE = "wow";
    public static final String DESCRIPTION = "wow descr";

    @Autowired
    ContentRepository repository;

    @Autowired
    CommentRepository commentRepository;

    UserService userService;

    @Autowired
    UserRepository userRepository;

    ContentService service;

    @Autowired
    GrammarCategoryRepository grammarCategoryRepository;

    @MockBean
    CommentService commentService;

    ModelMapper modelMapper;

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
        this.
                modelMapper = new ModelMapper();

        userService = new UserServiceImpl(userRepository,
                roleService,
                passwordEncoder,
                categoryWordsService,
                wordService,
                modelMapper);

        service = new ContentServiceImpl(modelMapper,
                commentService,
                repository,
                userService
        );
//        commentService = new CommentServiceImpl(commentRepository, modelMapper, userService);
    }


    @Test
    public void createContent_whenUserValidWithUser_shouldReturnContent() {
        User user = new User();
        user.setUsername("zxc");
        user.setEmail("zxc@zxc.zxc");
        user.setPassword("zxc@zxc.zxc");

        User save1 = userRepository.save(user);
        Content content = new Content();
        content.setTitle(TITLE);
        content.setAuthor(save1);
        content.setDescription(DESCRIPTION);
        Content save = repository.save(content);

        ContentServiceModel map = modelMapper.map(save, ContentServiceModel.class);

        ContentServiceModel serviceContent = service.createContent(map);

        assertEquals(TITLE, serviceContent.getTitle());
        assertEquals(DESCRIPTION, serviceContent.getDescription());
    }

    @Test
    public void getContentName_whenValid_shouldReturnContentName() {
        User user = new User();
        user.setUsername("zxc");
        user.setEmail("zxc@zxc.zxc");
        user.setPassword("zxc@zxc.zxc");

        User save1 = userRepository.save(user);
        Content content = new Content();
        content.setTitle(TITLE);
        content.setAuthor(save1);
        content.setDescription(DESCRIPTION);
        Content save = repository.save(content);

        ContentServiceModel map = modelMapper.map(save, ContentServiceModel.class);

        String contentName = service.getContentName(map.getId());

        assertEquals(TITLE, contentName);
    }


    @Test(expected = NoSuchElementException.class)
    public void getContentName_whenInvalid_shouldThrow() {
        service.getContentName("map.getId()");
    }

    @Test
    public void addCommentToContent_whenValid_shouldReturnComment() {
        CommentServiceModel model = new CommentServiceModel();

        model.setMessage("comment message");

        User user = new User();
        user.setUsername("zxc");
        user.setEmail("zxc@zxc.zxc");
        user.setPassword("zxc@zxc.zxc");
        User save = userRepository.save(user);
        UserServiceModel map = modelMapper.map(save, UserServiceModel.class);

        model.setUserServiceModel(map);

        User author = new User();
        author.setEmail("author@author.author");
        author.setUsername("author");
        author.setPassword("author");

        Content content = new Content();
        content.setAuthor(author);
        content.setTitle(TITLE);
        content.setDescription(DESCRIPTION);

        Content save1 = repository.save(content);

        ContentServiceModel contentServiceModel = modelMapper.map(save1, ContentServiceModel.class);

        Comment map1 = modelMapper.map(model, Comment.class);
        Comment save2 = commentRepository.save(map1);

        CommentServiceModel commentWithId = modelMapper.map(save2, CommentServiceModel.class);

        commentWithId.setContentServiceModel(contentServiceModel);

        when(commentService.addComment(commentWithId)).thenReturn(commentWithId);

        CommentServiceModel model1 = service.addCommentToContent(commentWithId);

        assertEquals("comment message", model1.getMessage());
    }

    @Test
    public void getContentByCategoryAndId_whenValidCategoryIdAndContentId_shouldReturnContent() {
        Content content = new Content();
        content.setTitle(TITLE);
        content.setDescription(DESCRIPTION);

        GrammarCategory category = new GrammarCategory();
        category.setName("grammar");



        Content save1 = repository.saveAndFlush(content);
        category.getContent().add(save1);
        GrammarCategory save = grammarCategoryRepository.save(category);
        save1.setCategory(save);
        repository.saveAndFlush(save1);

        ContentServiceModel contentByCategoryAndId = service.getContentByCategoryAndId(
                save.getId(), save1.getId());


        assertEquals(TITLE, contentByCategoryAndId.getTitle());
        assertEquals(DESCRIPTION, contentByCategoryAndId.getDescription());
        assertEquals(save.getName(), contentByCategoryAndId.getCategory().getName());
    }

    @Test
    public void getContentByCategoryAndId_whenNotFoundByIds_shouldReturnContentByName() {
        Content content = new Content();
        content.setTitle(TITLE);
        content.setDescription(DESCRIPTION);

        GrammarCategory category = new GrammarCategory();
        category.setName("grammar");

        Content save1 = repository.saveAndFlush(content);
        category.getContent().add(save1);
        GrammarCategory save = grammarCategoryRepository.save(category);
        save1.setCategory(save);
        repository.saveAndFlush(save1);

        ContentServiceModel contentByCategoryAndId = service.getContentByCategoryAndId(
                save.getName(), save1.getTitle());

        assertEquals(TITLE, contentByCategoryAndId.getTitle());
        assertEquals(DESCRIPTION, contentByCategoryAndId.getDescription());
        assertEquals(save.getName(), contentByCategoryAndId.getCategory().getName());
    }

    @Test(expected = NoContentFound.class)
    public void getContentByCategoryAndId_whenInvalid_shouldThrow() {
        service.getContentByCategoryAndId("category", "content");
    }
}
