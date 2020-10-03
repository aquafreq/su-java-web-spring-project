package com.example.english.web.interceptor;


import com.example.english.data.entity.Log;
import com.example.english.data.model.response.UserResponseModel;
import com.example.english.data.model.service.LogServiceModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.data.repository.LogRepository;
import com.example.english.service.LogService;
import com.example.english.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class LogInterceptor extends HandlerInterceptorAdapter {
    private final LogService logService;
    private final UserService userService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Principal userPrincipal = request.getUserPrincipal();

        if (userPrincipal != null) {
            String requestURI = request.getRequestURI();
            String method = request.getMethod();

            UserServiceModel user = userService.getUserById(userPrincipal.getName());

            LogServiceModel build = LogServiceModel
                    .builder()
                    .userId(user.getId())
                    .method(method)
                    .username(user.getUsername())
                    .url(requestURI)
                    .occurrence(LocalDateTime.now())
                    .build();

            logService.logMessage(build);
//            //tofix
//            UserServiceModel userByName = userService.getUserByName(userPrincipal.getName());
//            UserResponseModel responseModel = modelMapper.map(userByName, UserResponseModel.class);
//
//            response.setStatus(HttpStatus.OK.value());
//            String json = new ObjectMapper().writeValueAsString(responseModel);
//            response.getWriter().write(json);
//            response.flushBuffer();
        }

        super.afterCompletion(request, response, handler, ex);
    }
}

