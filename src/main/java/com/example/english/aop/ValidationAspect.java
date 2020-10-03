package com.example.english.aop;

import com.example.english.service.ValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class ValidationAspect {
    private final ValidationService validationService;

//    @Pointcut("execution(public * com.example.english.service.impl.*.*(..)) " +
//            "&& !(execution(* com.example.english.service.ValidationService.validate(..)) || " +
//            "execution(* com.example.english.service.LogService.*(..)))")

//    @Pointcut("execution(public * com.example.english.web.controller.*.*(..))")
//    public void validate() {
//    }

    @Pointcut("@annotation(com.example.english.annotations.Validate)")
    public void validate() {
    }

    @Before("validate()")
    public void validateServiceModel(JoinPoint point) {
        try {
            Object arg = point.getArgs()[0];
            validationService.validate(arg);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }
}
