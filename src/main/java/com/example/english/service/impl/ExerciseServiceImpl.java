//package com.example.english.service.impl;
//
//import com.example.english.data.entity.GrammarCategory;
//import com.example.english.data.model.service.ExerciseServiceModel;
//import com.example.english.data.model.service.GrammarCategoryServiceModel;
//import com.example.english.service.ExerciseService;
//import com.example.english.service.GrammarCategoryService;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class ExerciseServiceImpl implements ExerciseService {
//    private final ModelMapper modelMapper;
//    private final GrammarCategoryService grammarCategoryService;
//
//    @Override
//    public ExerciseServiceModel create(ExerciseServiceModel exerciseServiceModel) {
//
//        GrammarCategoryServiceModel grammarCategory = grammarCategoryService.getGrammarCategory(exerciseServiceModel.getCategory().getName());
//        GrammarCategory map1 = modelMapper.map(grammarCategory, GrammarCategory.class);
//
//        Exercise map = modelMapper.map(exerciseServiceModel, Exercise.class);
//        map.setCategory(map1);
//
//        return modelMapper.map(exerciseRepository.save(map), ExerciseServiceModel.class);
//    }
//}
