package com.example.english.controller;

import com.example.english.BaseControllerTest;
import com.example.english.data.entity.Content;
import com.example.english.data.entity.enumerations.LevelOfLanguage;
import com.example.english.data.model.binding.GrammarCategoryBindingModel;
import com.example.english.data.repository.ContentRepository;
import com.example.english.data.repository.GrammarCategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ContentControllerTests extends BaseControllerTest {
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
            GrammarCategoryBindingModel category = GrammarCategoryBindingModel
                    .builder()
                    .name("category")
                    .build();
            String json = new ObjectMapper().writeValueAsString(category);

            mockMvc
                    .perform(post(URL_PREFIX + "/category/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .characterEncoding("utf-8"))
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
