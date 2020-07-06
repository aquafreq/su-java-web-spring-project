package com.example.english.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

//Its pojo class that return jwt token string and if we need other field to send as response then need to declare a field in this file.
@Data
@RequiredArgsConstructor
public class JwtResponse implements Serializable {
    private final String jwttoken;

//    public JwtResponse(String jwttoken) {
//        this.jwttoken = jwttoken;
//    }

//    public String getToken() {
//        return this.jwttoken;
//    }
}