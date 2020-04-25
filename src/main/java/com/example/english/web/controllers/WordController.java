package com.example.english.web.controllers;

import com.example.english.domain.model.ServiceModelWord;
import com.example.english.service.WordService;
import com.example.english.web.models.WordCreateRequestModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller("/word")
public class WordController extends BaseController {

    private final WordService wordService;
    private final ModelMapper modelMapper;

    @Autowired
    public WordController(WordService wordService, ModelMapper modelMapper) {
        this.wordService = wordService;
        this.modelMapper = modelMapper;
    }

    ///{URIname}&{URIdefinition}
    @GetMapping("/create")
    public ModelAndView createWord() {
        return super.view("/create");
    }

    @PostMapping("/create")
    public void createWord(
//            @ModelAttribute("name") String name,
//                                   @ModelAttribute("definition") String definition
            @RequestBody WordCreateRequestModel word
//                                     @RequestParam String name,
//                                     @RequestParam String definition
//                                   @PathVariable String URIname,
//                                   @PathVariable String URIdefinition
    ) {
        ServiceModelWord wordService = this.modelMapper.map(word, ServiceModelWord.class);
        this.wordService.addWord(wordService);

        super.redirect("/words");
    }
}
