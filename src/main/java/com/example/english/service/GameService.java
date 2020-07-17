package com.example.english.service;

import com.example.english.data.entity.Word;
import com.example.english.data.model.ServiceModelWord;
import com.example.english.data.model.binding.WordBindingModel;
import com.example.english.data.model.service.GameServiceModel;

public interface GameService {
    GameServiceModel create(GameServiceModel gameServiceModel);
    GameServiceModel getGameByUserId(String id);
    void deleteGame(String id);
}
