package com.example.english.service.impl;

import com.example.english.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class ValidationServiceImpl implements ValidationService {
    private final Validator validator;

    @Override
    public <T> void validate(T model) {
        Set<ConstraintViolation<T>> validate = validator.validate(model);

        if (!validate.isEmpty()) {
            throw new ConstraintViolationException(validate);
        }
    }
}
