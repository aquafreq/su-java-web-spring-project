package com.example.english.web.interceptor;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static com.example.english.constants.SecurityConstants.*;
import static com.example.english.constants.SecurityConstants.TOKEN_PREFIX;

@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

}
