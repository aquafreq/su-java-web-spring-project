package com.example.english.service;


import com.example.english.data.entity.GrammarCategory;
import com.example.english.data.model.response.GrammarCategoryResponseModel;
import com.example.english.data.model.service.GrammarCategoryServiceModel;

import java.util.Collection;

public interface GrammarCategoryService {
    long getCount();

    void seedCategories(GrammarCategory... grammarCategory);

    GrammarCategoryServiceModel create(GrammarCategoryServiceModel map);

    GrammarCategoryServiceModel getGrammarCategory(String name);

    Collection<GrammarCategoryServiceModel> getAll();

}

