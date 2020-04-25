package com.example.english.service.impl;

import com.example.english.domain.entity.Word;
import com.example.english.domain.model.ServiceModelWord;
import com.example.english.repository.WordRepository;
import com.example.english.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WordServiceImpl implements WordService {
    private final WordRepository wordRepository;

    @Autowired
    public WordServiceImpl(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }


    @Override
    public void addWord(ServiceModelWord serviceModelWord) {
        Word word = new Word(serviceModelWord.getName(), serviceModelWord.getDefinition());

        this.wordRepository.save(word);
    }
}
