package com.example.english.web.controller;

import org.springframework.web.bind.annotation.RestController;

public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @Override
    public String getErrorPath() {
        return null;
    }
}
