package com.example.english.service.impl;

import com.example.english.data.entity.Word;
import com.example.english.data.entity.WordCategory;
import com.example.english.data.model.service.CategoryServiceModel;
import com.example.english.data.model.service.WordServiceModel;
import com.example.english.data.repository.WordCategoryRepository;
import com.example.english.service.WordCategoryService;
import com.example.english.service.WordService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class WordCategoryServiceImpl implements WordCategoryService {
    private final ModelMapper modelMapper;
    private final WordCategoryRepository categoryRepository;
    private final WordService wordService;

    @Override
    public CategoryServiceModel addCategory(CategoryServiceModel categoryServiceModel) {
        return null;
    }

    @Override
    public void removeCategoryById(String categoryId) {
//daa saa opraa 
    }

    @Override
    public List<WordServiceModel> getWordsByCategory(String categoryId) {
        return null;
    }

    @Override
    public void deleteWordsInCategory(List<String> words) {


    }

    @Override
    public void removeWordFromCategory(String categoryId, String wordId) {
        Word map = modelMapper.map(wordService.getWordById(wordId),
                Word.class);
        WordCategory wordCategory = categoryRepository.findById(categoryId)
                .orElseThrow();

        //da sa vidid
        wordCategory
                .getWords()
                .remove(map);

        categoryRepository.save(wordCategory);
    }

    @Override
    public CategoryServiceModel addWordToCategories(WordServiceModel wordServiceModel) {
        return modelMapper.map(modelMapper.map(
                wordService.getWordByNameAndDefinition(wordServiceModel.getName(), wordServiceModel.getDefinition()),
                Word.class), CategoryServiceModel.class);
    }
}
