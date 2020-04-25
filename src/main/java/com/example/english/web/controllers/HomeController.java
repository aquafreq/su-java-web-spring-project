package com.example.english.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends BaseController{

    @Autowired
    public HomeController() {
    }

    @GetMapping("/")
    public ModelAndView index() {
        return super.view("index");
    }

    @GetMapping("/home")
    public ModelAndView home() {
        return super.view("home");
    }
}
