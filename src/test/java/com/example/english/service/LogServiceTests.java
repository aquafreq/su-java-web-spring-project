package com.example.english.service;

import com.example.english.BaseTest;
import com.example.english.data.entity.Log;
import com.example.english.data.model.service.LogServiceModel;
import com.example.english.data.repository.LogRepository;
import com.example.english.service.impl.LogServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LogServiceTests extends BaseTest {

    ModelMapper modelMapper;

    @MockBean
    LogRepository repository;

    @MockBean
    UserService userService;

    LogService logService;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        logService = new LogServiceImpl(repository, userService, modelMapper);
    }

    @Test
    public void getLogs_shouldReturnAllLogs() {
        Log build = Log
                .builder()
                .method("GET")
                .url("/asd")
                .occurrence(LocalDateTime.now())
                .userId("1")
                .username("pepi")
                .build();

        Log build2 = Log
                .builder()
                .method("POST")
                .url("/zxc")
                .occurrence(LocalDateTime.now())
                .userId("2")
                .username("pepi2")
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(build, build2));

        Collection<LogServiceModel> logs = logService.getLogs();

        assertEquals(2, logs.size());
        assertTrue(logs.stream().anyMatch(x -> x.getUrl().equals("/zxc")));
        assertTrue(logs.stream().anyMatch(x -> x.getUrl().equals("/asd")));
    }

    @Test
    public void getLogsForUser_whenValid_shouldReturnLogs() {
        Log build = Log
                .builder()
                .method("GET")
                .url("/asd")
                .occurrence(LocalDateTime.now())
                .userId("1")
                .username("pepi")
                .build();

        when(repository.getLogsByUsername(anyString()))
                .thenReturn(Collections.singletonList(build));

        when(userService.getUserByUsername("1"))
                .thenReturn("wowez");

        List<LogServiceModel> pepi = new ArrayList<>(logService.getLogsForUser("1"));

        assertEquals(1, pepi.size());
        assertEquals(build.getUsername(), pepi.get(0).getUsername());
        assertEquals(build.getMethod(), pepi.get(0).getMethod());
        assertEquals(build.getOccurrence(), pepi.get(0).getOccurrence());
        assertEquals(build.getId(), pepi.get(0).getId());
    }

    @Test
    public void getLogsForUser_whenNoUser_shouldReturnEmptyCollection() {
        when(repository.getLogsByUsername(anyString()))
                .thenReturn(Collections.emptyList());
        List<LogServiceModel> pepi = new ArrayList<>(logService.getLogsForUser("1"));
        assertTrue(pepi.isEmpty());
    }

    @Test
    public void clearBrowsingLogs_whenThereAreLogs_shouldRemoveThem() {
        logService.clearBrowsingLogs();
        verify(repository, times(1)).deleteAllByMethod("GET");
    }
}
