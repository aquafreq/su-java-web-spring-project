package com.example.english.service.impl;

import com.example.english.data.entity.Content;
import com.example.english.data.entity.GrammarCategory;
import com.example.english.data.model.service.ContentServiceModel;
import com.example.english.data.model.service.GrammarCategoryServiceModel;
import com.example.english.data.repository.GrammarCategoryRepository;
import com.example.english.service.ContentService;
import com.example.english.service.GrammarCategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GrammarCategoryServiceImpl implements GrammarCategoryService {
    private final ModelMapper modelMapper;
    private final GrammarCategoryRepository grammarCategoryRepository;
    private final ContentService contentService;

    @Override
    public void seedCategories(GrammarCategory... grammarCategory) {
        Arrays.stream(grammarCategory).forEach(grammarCategoryRepository::save);
    }

    @Override
    public GrammarCategoryServiceModel create(GrammarCategoryServiceModel map) {
        if (grammarCategoryRepository.findByName(map.getName()).isPresent())
            throw new IllegalArgumentException(map.getName() + " already exists");

        GrammarCategory category = grammarCategoryRepository.save(modelMapper.map(map, GrammarCategory.class));

        return modelMapper.map(category, GrammarCategoryServiceModel.class);
    }

    @Override
    public Collection<GrammarCategoryServiceModel> getAll() {
        return getAllGrammarCategoryServiceModels();
    }

    private Collection<GrammarCategoryServiceModel> getAllGrammarCategoryServiceModels() {
        return grammarCategoryRepository.findAll()
                .stream()
                .map(x -> modelMapper.map(x, GrammarCategoryServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public GrammarCategoryServiceModel getGrammarCategory(String name) {
        GrammarCategory category = grammarCategoryRepository.findByName(name)
                .orElseGet(() ->
                        grammarCategoryRepository
                                .findAll()
                                .stream()
                                .filter(c -> compareNames(c.getName(), name))
                                .findAny()
                                .orElseThrow()
                );

        return modelMapper.map(category, GrammarCategoryServiceModel.class);
    }

    private boolean compareNames(String dbName, String searchName) {
        return dbName
                .toLowerCase()
                .replaceAll(" ", "-")
                .equals(searchName);
    }

    @Override
    public ContentServiceModel uploadContent(ContentServiceModel serviceModel) {
        GrammarCategory grammarCategory = grammarCategoryRepository
                .findById(serviceModel.getCategory().getId())
                .orElseThrow(() -> new NoSuchElementException("No such category!"));

        if (grammarCategory.getContent().stream().anyMatch(a -> a.getTitle().equals(serviceModel.getTitle())))
            throw new IllegalArgumentException(serviceModel.getTitle() + " already exists in this category!");

        ContentServiceModel serviceContent = contentService.createContent(serviceModel);

        Content map1 = modelMapper.map(serviceContent, Content.class);

        map1.setCategory(grammarCategory);
        grammarCategory.getContent().add(map1);

        grammarCategoryRepository.save(grammarCategory);

        return modelMapper.map(map1, ContentServiceModel.class);
    }

    @Override
    public GrammarCategoryServiceModel getGrammarCategoryById(String id) {
        GrammarCategory category = grammarCategoryRepository.findById(id).orElseThrow();
        return modelMapper.map(category, GrammarCategoryServiceModel.class);
    }

    @Override
    public String getGrammarCategoryName(String categoryId) {
        return grammarCategoryRepository.findById(categoryId)
                .map(GrammarCategory::getName)
                .orElseThrow();
    }

    @Override
    public long getCount() {
        return grammarCategoryRepository.count();
    }
}
