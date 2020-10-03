package com.example.english.service;

import com.example.english.data.entity.Log;
import com.example.english.data.model.service.LogServiceModel;

import java.util.Collection;
import java.util.List;

public interface LogService {
    void logMessage(LogServiceModel log);
    Collection<LogServiceModel> getLogs();
    Collection<LogServiceModel> getLogsForUser(String username);

    void clearBrowsingLogs();

    Collection<LogServiceModel>  getBrowsingLogs(String username);
}
