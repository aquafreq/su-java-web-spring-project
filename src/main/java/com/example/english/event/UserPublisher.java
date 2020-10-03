package com.example.english.event;


import com.example.english.data.model.response.UserResponseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void publishEvent(UserResponseModel userResponseModel) {
        UserRegistrationEvent userRegistrationEvent = new UserRegistrationEvent(this, userResponseModel);
        eventPublisher.publishEvent(userRegistrationEvent);
    }
}
