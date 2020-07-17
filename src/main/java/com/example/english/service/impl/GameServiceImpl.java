package com.example.english.service.impl;

import com.example.english.data.entity.Word;
import com.example.english.data.entity.WordGame;
import com.example.english.data.model.binding.WordBindingModel;
import com.example.english.data.model.service.GameServiceModel;
import com.example.english.data.repository.GameRepository;
import com.example.english.data.repository.WordRepository;
import com.example.english.service.GameService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public GameServiceModel create(GameServiceModel gameServiceModel) {
        WordGame map = modelMapper.map(gameServiceModel, WordGame.class);
        return modelMapper.map(repository.save(map), GameServiceModel.class);
    }

    @Override
    public GameServiceModel getGameByUserId(String id) {
        return modelMapper.map(repository.findById(id),
                GameServiceModel.class);
    }

    @Override
    public void deleteGame(String id) {
        repository.deleteById(id);
    }
}
