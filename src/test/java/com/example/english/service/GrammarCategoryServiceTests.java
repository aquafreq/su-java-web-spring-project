package com.example.english.service;

import com.example.english.BaseTest;
import com.example.english.data.entity.GrammarCategory;
import com.example.english.data.model.service.ContentServiceModel;
import com.example.english.data.model.service.GrammarCategoryServiceModel;
import com.example.english.data.repository.GrammarCategoryRepository;
import com.example.english.service.impl.GrammarCategoryServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class)
public class GrammarCategoryServiceTests extends BaseTest {

    ModelMapper modelMapper;

    @Autowired
    GrammarCategoryRepository repository;

    @MockBean
    ContentService contentService;

    GrammarCategoryService service;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        service = new GrammarCategoryServiceImpl(modelMapper, repository, contentService);
    }

    @Test
    public void seedCategories_withValidCategories_shouldSeedData() {
        GrammarCategory[] grammarCategories = getGrammarCategories();

        service.seedCategories(grammarCategories);
        Collection<GrammarCategoryServiceModel> all = service.getAll();

        assertEquals(grammarCategories.length, all.size());
    }

    private GrammarCategory[] getGrammarCategories() {
        return new GrammarCategory[]{
                new GrammarCategory("Present tense"),
                new GrammarCategory("Present perfect tense"),
                new GrammarCategory("Past tense"),
                new GrammarCategory("Future tense"),
                new GrammarCategory("Adjectives and adverbs"),
                new GrammarCategory("Conditionals"),
                new GrammarCategory("Passive and active voice"),
                new GrammarCategory("Reported Speech"),
                new GrammarCategory("Relative clauses"),
                new GrammarCategory("Nouns"),
        };
    }


    @Test
    public void create_withValidData_shouldCreateAndIncreaseSize() {
        GrammarCategoryServiceModel categoryServiceModel = new GrammarCategoryServiceModel();
        categoryServiceModel.setName("aaa");
        GrammarCategoryServiceModel categoryServiceModel1 =
                service.create(categoryServiceModel);

        assert categoryServiceModel1 != null;
        assertEquals("aaa", categoryServiceModel1.getName());
        assertEquals(1, repository.count());
    }

    @Test
    public void getGrammarCategory_withValidData_shouldReturnCategory() {
        GrammarCategory category = new GrammarCategory();
        category.setName("aaa");
        repository.save(category);

        GrammarCategoryServiceModel grammarCategory = service.getGrammarCategory(category.getName());
        assertEquals(category.getName(), grammarCategory.getName());
    }

    @Test
    public void getGrammarCategoryName_whenValidCategoryId_shouldReturnCategoryName() {
        GrammarCategory category = new GrammarCategory();
        category.setName("aaa");
        repository.save(category);

        String name = service.getGrammarCategoryName(category.getId());
        assertEquals(category.getName(), name);
    }

    @Test
    public void getGrammarCategoryById_whenValid_shouldReturnCorrectCategory() {
        GrammarCategory category = new GrammarCategory();
        category.setName("aaa");
        GrammarCategory save = repository.save(category);

        GrammarCategoryServiceModel grammarCategoryById = service.getGrammarCategoryById(save.getId());

        assertEquals("aaa", grammarCategoryById.getName());
    }

    @Test
    public void getGrammarCategory_withLowerCaseDashedName_shouldReturnCategory() {
        GrammarCategory category = new GrammarCategory();
        category.setName("A A A");
        repository.save(category);

        GrammarCategoryServiceModel grammarCategory = service.getGrammarCategory("a-a-a");

        assertEquals("A A A", grammarCategory.getName());
    }

    @Test
    public void getGrammarCategory_withNoSuchName_shouldThrow() {
        assertThrows(NoSuchElementException.class,
                () -> service.getGrammarCategory("zxc"));
    }

    @Test
    public void getAll_whenValidInput_shouldReturnAll() {
        List<GrammarCategoryServiceModel> actual =
                Arrays.stream(getGrammarCategories())
                        .map(x -> repository.save(x))
                        .map(x -> modelMapper.map(x, GrammarCategoryServiceModel.class))
                        .collect(Collectors.toList());

        Collection<GrammarCategoryServiceModel> expected = service.getAll();

        assertEquals(actual, expected);
        assertEquals(actual.size(), service.getCount());
    }

    @Test
    public void uploadContent_whenInvalidCategory_shouldThrow() {
        GrammarCategoryServiceModel grammarCategoryServiceModel = new GrammarCategoryServiceModel();
        grammarCategoryServiceModel.setName("A A A");
        grammarCategoryServiceModel.setId("A A A");

        ContentServiceModel serviceModel = new ContentServiceModel();
        serviceModel.setTitle("title");
        serviceModel.setDescription("desc");
        serviceModel.setCategory(grammarCategoryServiceModel);

        when(contentService.createContent(any(ContentServiceModel.class)))
                .thenReturn(serviceModel);

        assertThrows(NoSuchElementException.class,
                () -> service.uploadContent(serviceModel));
    }


    @Test
    public void uploadContent_whenValidContent_shouldReturnContent() {
        GrammarCategoryServiceModel grammarCategoryServiceModel = new GrammarCategoryServiceModel();
        grammarCategoryServiceModel.setName("A A A");
        GrammarCategoryServiceModel categoryServiceModel =
                service.create(grammarCategoryServiceModel);


        ContentServiceModel serviceModel = new ContentServiceModel();
        serviceModel.setTitle("title");
        serviceModel.setDescription("desc");
        serviceModel.setCategory(categoryServiceModel);

        when(contentService.createContent(any(ContentServiceModel.class)))
                .thenReturn(serviceModel);

        ContentServiceModel contentServiceModel = service.uploadContent(serviceModel);

        assertEquals("title", contentServiceModel.getTitle());
        assertEquals("desc", contentServiceModel.getDescription());
        assertEquals("A A A", contentServiceModel.getCategory().getName());
        assertEquals(1, contentServiceModel.getCategory().getContent().size());
    }
}
