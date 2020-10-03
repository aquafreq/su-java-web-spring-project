package com.example.english.exceptions.handler;

import com.example.english.data.model.response.ErrorResponseModel;
import com.example.english.exceptions.NoContentFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class GlobalExceptionHandler {

    //catches expected cases
    @ExceptionHandler({NoContentFound.class,
            UsernameNotFoundException.class,
            NoSuchElementException.class,
            IllegalArgumentException.class})
    @ResponseBody
    public ErrorResponseModel handleException(Exception exception) {
        return ErrorResponseModel
                .builder()
                .statusCode(404)
                .message(exception.getMessage())
                .build();
    }

    //catches the unexpected
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ErrorResponseModel handleGlobalException(Exception exception) {
        return ErrorResponseModel
                .builder()
                .statusCode(401)
                .message(exception.getMessage())
                .build();
    }

    //catches errors from exceptions in service validations done in AOP
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, List<String>>> validationExceptionHandler(Exception exception) {
        Map<String, List<String>> errors = ((ConstraintViolationException) exception).getConstraintViolations()
                .stream()
                .collect(Collectors.groupingBy(k -> k.getPropertyPath().toString(),
                        Collectors.mapping(ConstraintViolation::getMessage, toList())));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
