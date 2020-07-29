package com.example.english.service;

import com.example.english.data.model.service.WordServiceModel;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface WordService {
    WordServiceModel createWord(WordServiceModel wordServiceModel);
    WordServiceModel deleteWordById(String id);
    List<WordServiceModel> addWords(List<WordServiceModel> words);

    WordServiceModel getWordByNameAndDefinition(String name, String definition);

    WordServiceModel getWordByIdOrName(String wordId);

    Collection<WordServiceModel> getWordsById(List<String> words);
}
