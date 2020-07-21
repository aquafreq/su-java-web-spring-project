package com.example.english.service;


import com.example.english.data.model.service.CategoryWordsServiceModel;
import com.example.english.data.model.service.WordServiceModel;

import java.util.List;

public interface CategoryWordsService {
    CategoryWordsServiceModel addCategory(CategoryWordsServiceModel categoryWordsServiceModel);
    void removeCategoryById(String categoryId);
    CategoryWordsServiceModel getCategoryWord(String categoryId);

    void deleteWordsInCategory(String categoryId, List<String> words);

    void removeWordFromCategory(String categoryId, String wordId);

    CategoryWordsServiceModel addWordToCategory(WordServiceModel wordServiceModel);
}
