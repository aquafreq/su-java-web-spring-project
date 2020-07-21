package com.example.english.service.impl;

import com.example.english.data.entity.Content;
import com.example.english.data.entity.GrammarCategory;
import com.example.english.data.model.response.GrammarCategoryResponseModel;
import com.example.english.data.model.service.ContentServiceModel;
import com.example.english.data.model.service.GrammarCategoryServiceModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.data.repository.GrammarCategoryRepository;
import com.example.english.service.ContentService;
import com.example.english.service.GrammarCategoryService;
import com.example.english.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
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
        GrammarCategory map1 = modelMapper.map(map, GrammarCategory.class);
        return modelMapper.map(grammarCategoryRepository.save(map1), GrammarCategoryServiceModel.class);
    }

    @Override
    public GrammarCategoryServiceModel getGrammarCategory(String name) {
        //just in case
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
    public Collection<GrammarCategoryServiceModel> getAll() {
        return grammarCategoryRepository.findAll()
                .stream()
                .map(x -> modelMapper.map(x, GrammarCategoryServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ContentServiceModel uploadContent(ContentServiceModel serviceModel) {
        UserServiceModel author = serviceModel.getAuthor();
//        userService.getUserByName()
//        serviceModel.getAuthor()
        Content map = modelMapper.map(contentService.createContent(serviceModel), Content.class);

//        map.setCategory();
//        modelMapper.map();
        return null;
    }

    @Override
    public long getCount() {
        return grammarCategoryRepository.count();
    }
}