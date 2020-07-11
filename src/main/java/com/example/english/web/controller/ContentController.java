package com.example.english.web.controller;

import com.example.english.data.model.binding.ExerciseBindingModel;
import com.example.english.data.model.binding.GrammarCategoryBindingModel;
import com.example.english.data.model.response.ExerciseResponseModel;
import com.example.english.data.model.response.GrammarCategoryResponseModel;
import com.example.english.data.model.service.ExerciseServiceModel;
import com.example.english.data.model.service.GrammarCategoryServiceModel;
import com.example.english.service.ExerciseService;
import com.example.english.service.GrammarCategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/content")
@RequiredArgsConstructor
public class ContentController {
    private final GrammarCategoryService grammarCategoryService;
    private final ExerciseService exerciseService;
    private final ModelMapper modelMapper;

//    @PreAuthorize(value = "hasRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PostMapping("/create/category")
    public ResponseEntity<GrammarCategoryResponseModel> createCategory(@RequestBody GrammarCategoryBindingModel grammarCategoryBindingModel, UriComponentsBuilder builder) {
        GrammarCategoryResponseModel categoryResponseModel =
                modelMapper.map(grammarCategoryService.create(
                        modelMapper.map(grammarCategoryBindingModel, GrammarCategoryServiceModel.class)),
                        GrammarCategoryResponseModel.class);

        ResponseEntity.BodyBuilder created = ResponseEntity.created(builder.path("content/create/category/" + categoryResponseModel.getId()).build().toUri());
        return created.body(categoryResponseModel);
    }

//    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @PostMapping("/create/exercise")
    public ResponseEntity<ExerciseResponseModel> createExercise(ExerciseBindingModel exerciseBindingModel, UriComponentsBuilder builder) {
        ExerciseServiceModel exerciseServiceModel = exerciseService
                .create(modelMapper.map(exerciseBindingModel, ExerciseServiceModel.class));
        ExerciseResponseModel exerciseResponseModel = modelMapper
                .map(exerciseServiceModel, ExerciseResponseModel.class);

        ResponseEntity.BodyBuilder created =
                ResponseEntity.created(builder.path("categories/" + exerciseServiceModel.getId()).build().toUri());
        return created.body(exerciseResponseModel);
    }


//    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("/all/categories")
    public ResponseEntity<List<GrammarCategoryResponseModel>> getAllGrammarCategories() {
        List<GrammarCategoryResponseModel> categoryResponseModelList =
                grammarCategoryService.getAll()
                        .stream()
                        .map(c -> modelMapper.map(c, GrammarCategoryResponseModel.class))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(categoryResponseModelList);
    }

    //remove category, edit category
    //remove exercise, edit exercise
}
