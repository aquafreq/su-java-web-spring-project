package com.example.english.service.impl;

import com.example.english.data.entity.Word;
import com.example.english.data.model.service.WordServiceModel;
import com.example.english.data.repository.WordRepository;
import com.example.english.service.WordService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {
    private final ModelMapper modelMapper;
    private final WordRepository repository;

    @Override
    public WordServiceModel createWord(WordServiceModel wordServiceModel) {
        Word map = modelMapper.map(wordServiceModel,
                Word.class);
        return modelMapper
                .map(repository.save(map), WordServiceModel.class);
    }

    @Override
    public WordServiceModel deleteWordById(String id) {
        Word word = repository.findById(id).orElseThrow();
        repository.deleteById(id);
        return modelMapper.map(word, WordServiceModel.class);
    }

    @Override
    public List<WordServiceModel> addWords(List<WordServiceModel> words) {
        List<Word> collect = words
                .stream()
                .map(w -> modelMapper.map(w, Word.class))
                .collect(Collectors.toList());

        return repository.saveAll(collect)
                .stream()
                .map(w -> modelMapper.map(w, WordServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public WordServiceModel getWordByNameAndDefinition(String name, String definition) {
        return modelMapper.map(repository.getByNameAndDefinition(name, definition),
                WordServiceModel.class);
    }

    @Override
    public WordServiceModel getWordByIdOrName(String wordId) {
        return modelMapper.map(repository.getByIdOrName(wordId, wordId), WordServiceModel.class);
    }

    @Override
    public Collection<WordServiceModel> getWordsById(List<String> words) {
        return repository
                .findAll()
                .stream()
                .filter(w -> words.contains(w.getId()))
                .map(w -> modelMapper.map(w, WordServiceModel.class))
                .collect(Collectors.toList());
    }
}
