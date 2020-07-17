package com.example.english.service.impl;

import com.example.english.data.entity.Log;
import com.example.english.data.repository.LogRepository;
import com.example.english.service.LogService;
import com.example.english.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@XSlf4j
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;
    private final UserService userService;


    @Override
    public void logMessage(Log log) {
        logRepository.save(log);
    }

    @Override
    public List<Log> getLogs() {
        return logRepository.findAll();
    }

    @Override
    public List<Log> getLogsForUser(String id){
        String username = userService.getUserByUsername(id);
        return logRepository.getByUsername(username);
    }
}
