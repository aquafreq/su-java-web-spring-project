package com.example.english.data.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LogResponseModel {
    private String username;
    private String id;
    private String userId;
    private String url;
    private String method;
    private String occurrence;
}
