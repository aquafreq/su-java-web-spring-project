package com.example.english.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//It’s a pojo class that contains a username and password to get a request data in an authentication method.
@NoArgsConstructor
@Data
@AllArgsConstructor
public class JwtRequest implements Serializable {

    private String username;
    private String password;

//    // need default constructor for JSON Parsing
//    public JwtRequest() {
//
//    }
//
//    public JwtRequest(String username, String password) {
//        this.setUsername(username);
//        this.setPassword(password);
//    }
//
//    public String getUsername() {
//        return this.username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return this.password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
}