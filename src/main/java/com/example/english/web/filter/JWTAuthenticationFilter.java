package com.example.english.web.filter;

import com.example.english.data.entity.Role;
import com.example.english.data.entity.User;
import com.example.english.data.model.binding.UserLoginBindingModel;
import com.example.english.data.model.response.UserResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import static com.example.english.constants.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res)
            throws AuthenticationException {

        try {
            UserLoginBindingModel creds = new ObjectMapper()
                    .readValue(req.getInputStream(), UserLoginBindingModel.class);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword());

            return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (IOException ignored) {
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        User user = ((User) auth.getPrincipal());

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("authorities", user.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        UserResponseModel map = new UserResponseModel(
                user.getId(),
                user.getUsername(),
                user.getAuthorities()
                        .stream()
                        .map(Role::getAuthority)
                        .collect(Collectors.toList())
        );

        String s = map.toString();

        Cookie cookie = new Cookie("user", s);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        Cookie cookie1 = (Cookie) cookie.clone();
        cookie1.setPath("/");
        cookie1.setHttpOnly(false);

        res.addCookie(cookie);
        cookie1.setDomain("");
        res.addCookie(cookie1);
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
//        chain.doFilter(req,res);
    }
}