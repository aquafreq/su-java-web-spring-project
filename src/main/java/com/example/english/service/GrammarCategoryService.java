package com.example.english.service;


import com.example.english.data.entity.GrammarCategory;
import com.example.english.data.model.service.ContentServiceModel;
import com.example.english.data.model.service.GrammarCategoryServiceModel;

import java.util.Collection;

public interface GrammarCategoryService {
    long getCount();

    void seedCategories(GrammarCategory... grammarCategory);

    GrammarCategoryServiceModel create(GrammarCategoryServiceModel map);

    GrammarCategoryServiceModel getGrammarCategory(String name);

    Collection<GrammarCategoryServiceModel> getAll();

    ContentServiceModel uploadContent(ContentServiceModel serviceModel);

    GrammarCategoryServiceModel getGrammarCategoryById(String id);

    String getGrammarCategoryName(String categoryId);

}

