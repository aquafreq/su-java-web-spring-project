package com.example.english.service;

import org.springframework.context.annotation.Profile;

@Profile("!test")
public interface EmailService {
    void sendMessage(String to, String subject, String text);
}
