package com.example.english.service.impl;

import com.example.english.data.entity.Word;
import com.example.english.data.model.ServiceModelWord;
import com.example.english.data.model.binding.WordBindingModel;
import com.example.english.data.repository.WordRepository;
import com.example.english.service.WordService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {
    private final WordRepository wordRepository;
    private final ModelMapper modelMapper;

    @Override
    public Word create(WordBindingModel word) {
        return wordRepository.save(modelMapper.map(word, Word.class));
    }
}
