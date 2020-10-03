package com.example.english.event;

import com.example.english.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.example.english.constants.EmailConstants.SUBJECT;
import static com.example.english.constants.EmailConstants.TEXT;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class UserRegistrationListener {
    private final EmailService emailService;

    @EventListener(UserRegistrationEvent.class)
    public void onUserRegistration(UserRegistrationEvent registrationEvent) {
        String toEmail = registrationEvent.getUserResponseModel().getEmail();

        log.info(toEmail);

        emailService.sendMessage(registrationEvent.getUserResponseModel().getEmail(),
                SUBJECT, TEXT);
    }
}
