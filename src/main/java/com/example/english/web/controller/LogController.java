package com.example.english.web.controller;


import com.example.english.data.model.response.LogResponseModel;
import com.example.english.data.model.service.LogServiceModel;
import com.example.english.service.LogService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RequestMapping(value = "/logs")
@RestController
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;
    private final ModelMapper modelMapper;

    @PreAuthorize(value = "hasAnyRole('ROOT_ADMIN','ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Collection<LogResponseModel>> getAllLogs() {
        return ResponseEntity.ok(logService.getLogs()
                .stream()
                .sorted((e2,e1) -> e1.getOccurrence().compareTo(e2.getOccurrence()))
                .map(x -> {
                    LogResponseModel map = modelMapper.map(x, LogResponseModel.class);
                    map.setOccurrence(mapLocalDateTimeToString(x.getOccurrence().toString()));
                    return map;
                })
                .collect(Collectors.toList()));
    }

    private String mapLocalDateTimeToString(String localDateTime) {
        String substring = localDateTime.substring(0, localDateTime.lastIndexOf("."));
        return substring.replaceAll("T", " ");
    }

    @PreAuthorize(value = "hasAnyRole('ROOT_ADMIN','ADMIN')")
    @GetMapping("/{username}")
    public ResponseEntity<Collection<LogResponseModel>> getLogsForUser(@PathVariable String username) {
        return ResponseEntity.ok(logService.getLogsForUser(username)
                .stream()
                .sorted((e2,e1) -> e1.getOccurrence().compareTo(e2.getOccurrence()))
                .map(x -> {
                    LogResponseModel map = modelMapper.map(x, LogResponseModel.class);
                    map.setOccurrence(mapLocalDateTimeToString(x.getOccurrence().toString()));
                    return map;
                })
                .collect(Collectors.toList()));
    }
}
