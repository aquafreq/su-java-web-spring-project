package com.example.english.controller;

import antlr.build.ANTLR;
import com.example.english.EnglishApplication;
import com.example.english.data.entity.Content;
import com.example.english.data.entity.GrammarCategory;
import com.example.english.data.entity.enumerations.LevelOfLanguage;
import com.example.english.data.model.binding.GrammarCategoryBindingModel;
import com.example.english.data.repository.ContentRepository;
import com.example.english.data.repository.GrammarCategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.init.ResourceReaderRepositoryPopulator;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ContentControllerTests {
    private final String URL_PREFIX = "/content";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    GrammarCategoryRepository grammarCategoryRepository;

    @Test
    public void getLevelOfLanguages_shouldReturnAllLevels() throws Exception {
        List<String> collect = Arrays.stream(LevelOfLanguage.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        mockMvc.perform(get(URL_PREFIX + "/category/levels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("*", is(collect)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MODERATOR", "ROOT_ADMIN"})
    public void createCategory_whenValid_shouldReturnCreated() throws Exception {
        GrammarCategoryBindingModel category = GrammarCategoryBindingModel.builder().name("category").build();
        mockMvc
                .perform(post(URL_PREFIX + "/category/create")
                        .content(new ObjectMapper().writeValueAsString(category))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("category"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void createCategory_whenNotAuthorized_shouldReturn401() throws Exception {
        GrammarCategoryBindingModel category = GrammarCategoryBindingModel.builder().name("category").build();
        mockMvc.perform(post(URL_PREFIX + "/category/create")
                .content(new ObjectMapper().writeValueAsString(category))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode", is(401)))
                .andExpect(jsonPath("$.message").value("Access is denied"))
                .andReturn();
    }

    @Test
    public void getAllGrammarCategories_shouldReturnAllCategories() throws Exception {
        long count = grammarCategoryRepository.count();

        mockMvc.perform(
                get(URL_PREFIX + "/category/all-categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize((int) count)))
                .andReturn();
    }

    @Test
    public void getGrammarCategoryContent_whenValidName_shouldReturnCategory() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/category/nouns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Nouns")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getContent_whenValidCategoryIdAndContentId_shouldReturnContent() throws Exception {
        Content content = contentRepository.findByTitle("What is Lorem Ipsum?");
        String categoryId = content.getCategory().getId();
        String contentId = content.getId();
        String url = URL_PREFIX + String.format("/category/%s/%s", categoryId, contentId);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(content.getTitle())))
                .andExpect(jsonPath("$.description", is(content.getDescription())))
                .andExpect(jsonPath("$.categoryName", is(content.getCategory().getName())));
    }

    @Test
    public void getContent_whenValidCategoryNameAndContentName_shouldReturnContent() throws Exception {
        Content content = contentRepository.findByTitle("What is Lorem Ipsum?");
        String categoryName = content.getCategory().getName();
        String contentId = content.getTitle();

        String url = URL_PREFIX + String.format("/category/%s/%s", categoryName, contentId);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(content.getTitle())))
                .andExpect(jsonPath("$.description", is(content.getDescription())))
                .andExpect(jsonPath("$.categoryName", is(content.getCategory().getName())));
    }

    @Test
    public void getContent_whenInvalidCategory_shouldReturnNoContentFound() throws Exception {
        Content content = contentRepository.findByTitle("What is Lorem Ipsum?");
        String categoryName = content.getCategory().getName();

        String url = URL_PREFIX + String.format("/category/%s/invalid", categoryName);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(MvcResult::getResolvedException)
                .andExpect(jsonPath("$.statusCode", is(404)));
    }
}
