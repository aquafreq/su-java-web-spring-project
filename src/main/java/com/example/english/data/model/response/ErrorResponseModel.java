package com.example.english.data.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ErrorResponseModel {
    private String message;
    private int statusCode;
    private List<String> errors;

    public ErrorResponseModel setMessage(String message) {
        this.message = message;
        return this;
    }

    public ErrorResponseModel setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }
}
