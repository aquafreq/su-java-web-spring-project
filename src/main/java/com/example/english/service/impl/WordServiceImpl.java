package com.example.english.service.impl;

import com.example.english.data.entity.Word;
import com.example.english.data.model.service.WordServiceModel;
import com.example.english.data.repository.WordRepository;
import com.example.english.service.WordService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.english.constants.WordConstants.*;

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {
    private final ModelMapper modelMapper;
    private final WordRepository repository;

    @Override
    public WordServiceModel createWord(WordServiceModel wordServiceModel) {
        Word map = modelMapper.map(wordServiceModel, Word.class);
        return modelMapper.map(repository.save(map), WordServiceModel.class);
    }

    @Override
    public WordServiceModel deleteWordById(String id) {
        Word word = repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NO_SUCH_WORD));

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

        Word word = repository.getByNameAndDefinition(name, definition)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format(
                                NO_SUCH_WORD_WITH_NAME_AND_DEFINITION,
                                name, definition)));

        return modelMapper.map(word, WordServiceModel.class);
    }

    @Override
    public WordServiceModel getWordByIdOrName(String wordId) {
        Word word = repository.getByIdOrName(wordId, wordId)
                .orElseThrow(() -> new EntityNotFoundException(NO_SUCH_WORD));

        return modelMapper.map(word, WordServiceModel.class);
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
