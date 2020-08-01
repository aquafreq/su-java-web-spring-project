package com.example.english.exceptions.handler;

import com.example.english.data.model.response.ErrorResponseModel;
import com.example.english.exceptions.NoContentFound;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NoContentFound.class, UsernameNotFoundException.class, NoSuchElementException.class})
    @ResponseBody
    public ErrorResponseModel handleException(Exception exception) {
        return ErrorResponseModel
                .builder()
                .statusCode(404)
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResponseModel handleGlobalException(Exception exception) {
        return ErrorResponseModel
                .builder()
                .statusCode(401)
                .message(exception.getMessage())
                .build();
    }
}
