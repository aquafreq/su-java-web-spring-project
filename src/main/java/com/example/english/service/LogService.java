package com.example.english.service;

import com.example.english.data.entity.Log;

import java.util.List;

public interface LogService {
    void logMessage(Log log);
    List<Log> getLogs();
    List<Log> getLogsForUser(String id);
}
