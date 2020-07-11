package com.example.english.service.impl;

import com.example.english.data.entity.GrammarCategory;
import com.example.english.data.model.response.GrammarCategoryResponseModel;
import com.example.english.data.model.service.GrammarCategoryServiceModel;
import com.example.english.data.repository.GrammarCategoryRepository;
import com.example.english.service.GrammarCategoryService;
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
        return
                modelMapper.map(grammarCategoryRepository.findByName(name), GrammarCategoryServiceModel.class);
    }

    @Override
    public Collection<GrammarCategoryServiceModel> getAll() {
        return grammarCategoryRepository.findAll()
                .stream()
                .map(x -> modelMapper.map(x, GrammarCategoryServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public long getCount() {
        return grammarCategoryRepository.count();
    }
}
