package com.example.english.service;


import com.example.english.data.model.service.CategoryServiceModel;
import com.example.english.data.model.service.WordServiceModel;

import java.util.List;

public interface WordCategoryService {
    CategoryServiceModel addCategory(CategoryServiceModel categoryServiceModel);
    void removeCategoryById(String categoryId);
    List<WordServiceModel> getWordsByCategory(String categoryId);
    void deleteWordsInCategory(List<String> words);

    void removeWordFromCategory(String categoryId, String wordId);

    CategoryServiceModel addWordToCategories(WordServiceModel wordServiceModel);
}
