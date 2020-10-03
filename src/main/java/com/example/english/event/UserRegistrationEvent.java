package com.example.english.event;

import com.example.english.data.model.response.UserResponseModel;
import org.springframework.context.ApplicationEvent;

public class UserRegistrationEvent extends ApplicationEvent {
    private final UserResponseModel userResponseModel;

    public UserRegistrationEvent(Object source, UserResponseModel responseModel) {
        super(source);
        this.userResponseModel = responseModel;
    }

    public UserResponseModel getUserResponseModel() {
        return userResponseModel;
    }
}
