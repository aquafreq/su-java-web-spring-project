package com.example.english.data.model.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogServiceModel {
    private String username;
    private String id;
    private String userId;
    private String url;
    private String method;
    private LocalDateTime occurrence;
}
