package com.example.english.exceptions.handler;

import com.example.english.data.model.response.ErrorResponseModel;
import com.example.english.exceptions.NoContentFound;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NoContentFound.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponseModel> handleException(Exception exception) {
        ErrorResponseModel errorResponseModel =
                new ErrorResponseModel()
                .setMessage(exception.getMessage())
                .setStatusCode(400);

        return ResponseEntity.status(errorResponseModel.getStatusCode()).body(errorResponseModel);
    }
}
