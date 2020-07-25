package com.example.english.service.impl;

import com.example.english.data.entity.Word;
import com.example.english.data.entity.CategoryWords;
import com.example.english.data.model.service.CategoryWordsServiceModel;
import com.example.english.data.model.service.WordServiceModel;
import com.example.english.data.repository.CategoryWordsRepository;
import com.example.english.service.CategoryWordsService;
import com.example.english.service.UserService;
import com.example.english.service.WordService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryWordsServiceImpl implements CategoryWordsService {
    private final ModelMapper modelMapper;
    private final CategoryWordsRepository repository;
    private final WordService wordService;

    @Override
    public CategoryWordsServiceModel addCategory(String category) {
        CategoryWords categoryWords = new CategoryWords(category);

        return modelMapper.map(repository.save(categoryWords),
                CategoryWordsServiceModel.class);
    }

    @Override
    public void removeCategoryById(String categoryId) {
        repository.deleteById(categoryId);
    }

    @Override
    public CategoryWordsServiceModel getCategoryWord(String categoryId) {
//        repository.findById(categoryId)
        return null;
    }

    @Override
    public void deleteWordsInCategory(String categoryId, List<String> words) {
        repository.findById(categoryId)
                .ifPresent(c -> {
                    c.getWords().removeAll(
                            wordService
                                    .getWordsById(words)
                                    .stream()
                                    .map(w -> modelMapper.map(w, Word.class))
                                    .collect(Collectors.toList())
                    );
                });
    }

    @Override
    public void removeWordFromCategory(String categoryId, String wordId) {
        Word map = modelMapper.map(wordService.getWordById(wordId),
                Word.class);

        CategoryWords categoryWords = repository.findById(categoryId)
                .orElseThrow();

        //da sa vidid
        categoryWords
                .getWords()
                .remove(map);

        repository.save(categoryWords);
    }
}
