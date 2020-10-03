package com.example.english.service.impl;

import com.example.english.data.entity.Log;
import com.example.english.data.model.service.LogServiceModel;
import com.example.english.data.repository.LogRepository;
import com.example.english.service.LogService;
import com.example.english.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl implements LogService {
    public static final String METHOD = "GET";
    private final LogRepository logRepository;
    private final UserService userService;
    private final ModelMapper mapper;

    public LogServiceImpl(LogRepository logRepository, @Lazy UserService userService, ModelMapper mapper) {
        this.logRepository = logRepository;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public void logMessage(LogServiceModel log) {
        logRepository.save(mapper.map(log, Log.class));
    }

    @Override
    public Collection<LogServiceModel> getLogs() {
        return logRepository
                .findAll()
                .stream()
                .map(x -> mapper.map(x, LogServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<LogServiceModel> getLogsForUser(String id) {
        String username = userService.getUserByUsername(id);

        return logRepository
                .getLogsByUsername(username)
                .stream()
                .map(x -> mapper.map(x, LogServiceModel.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void clearBrowsingLogs() {
        logRepository.deleteAllByMethod(METHOD);
    }

    @Override
    public Collection<LogServiceModel>  getBrowsingLogs(String username) {
        return logRepository.getLogsByMethodAndUsername(METHOD,username)
                .stream()
                .map(x-> mapper.map(x,LogServiceModel.class))
                .collect(Collectors.toList());
    }
}
