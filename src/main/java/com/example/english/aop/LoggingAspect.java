//package com.example.english.aop;
//
//import com.example.english.data.entity.Log;
//import com.example.english.service.LogService;
//import lombok.AllArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.lang.reflect.Method;
//import java.time.LocalDateTime;
//
//@Aspect
//@Component
//@Order(0)
//@RequiredArgsConstructor
//public class LoggingAspect {
//    private final LogService logService;
//    private final HttpServletResponse response;
//    private final HttpServletRequest request;
//
////    @Around(value = "within(com.example.english.web.controller *)", argNames = "pjp,point")
////    public Object saveLog(ProceedingJoinPoint pjp, JoinPoint point) throws Throwable {
////        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////        MethodSignature methodSignature = (MethodSignature) point.getSignature();
////        Method method = methodSignature .getMethod();
////        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
////
////        Object principal = auth.getPrincipal();
////        System.out.println();
////        Object proceed = pjp.proceed();
////
////        return proceed;
////    }
//
////    @Around(value = "within(@org.springframework.stereotype.Controller *)", argNames = "pjp")
////    public Object getcontroller(ProceedingJoinPoint pjp) throws Throwable {
////
////        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
////        Method method = methodSignature .getMethod();
////        RequestMethod[] requestMethods = method.getAnnotation(RequestMapping.class).method();
////        System.out.println(this);
////        Object proceed = pjp.proceed();
////        return proceed;
////    }
//
//    @After("within(@org.springframework.stereotype.Controller *)")
//    public void getcontrollerbefore(){
//        System.out.println(this);
//        System.out.println(response);
//        System.out.println(request);
//        System.out.println(this);
////        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
////        Method method = methodSignature .getMethod();
////        RequestMethod[] requestMethods = method.getAnnotation(RequestMapping.class).method();
////        System.out.println(this);
////        void proceed = pjp.proceed();
////        return proceed;
//    }
//    private Log createLog(String url, String method, String username, LocalDateTime occurrence) {
//        Log build = Log.builder()
//                .method(method)
//                .username(username)
//                .occurrence(occurrence)
//                .build();
//
//        logService.logMessage(build);
//        return build;
//    }
//}
