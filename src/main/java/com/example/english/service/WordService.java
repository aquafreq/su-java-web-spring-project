package com.example.english.service;

import com.example.english.data.entity.Word;
import com.example.english.data.model.ServiceModelWord;
import com.example.english.data.model.binding.WordBindingModel;

public interface WordService {
    Word create(WordBindingModel word);
}
