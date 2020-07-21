package com.example.english.web.controller;

import com.example.english.data.model.binding.CommentBindingModel;
import com.example.english.data.model.binding.ContentBindingModel;
import com.example.english.data.model.binding.ExerciseBindingModel;
import com.example.english.data.model.binding.GrammarCategoryBindingModel;
import com.example.english.data.model.response.*;
import com.example.english.data.model.service.CommentServiceModel;
import com.example.english.data.model.service.ContentServiceModel;
import com.example.english.data.model.service.ExerciseServiceModel;
import com.example.english.data.model.service.GrammarCategoryServiceModel;
import com.example.english.service.ContentService;
import com.example.english.service.ExerciseService;
import com.example.english.service.GrammarCategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/content")
@RequiredArgsConstructor
public class ContentController {
    private final GrammarCategoryService grammarCategoryService;
    private final ExerciseService exerciseService;
    private final ContentService contentService;
    private final ModelMapper modelMapper;

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/category/create")
    public ResponseEntity<GrammarCategoryResponseModel> createCategory(@RequestBody GrammarCategoryBindingModel grammarCategoryBindingModel, UriComponentsBuilder builder) {
        GrammarCategoryResponseModel categoryResponseModel =
                modelMapper.map(grammarCategoryService.create(
                        modelMapper.map(grammarCategoryBindingModel, GrammarCategoryServiceModel.class)),
                        GrammarCategoryResponseModel.class);

        ResponseEntity.BodyBuilder created = ResponseEntity.created(builder.path("content/create/category/" + categoryResponseModel.getId()).build().toUri());
        return created.body(categoryResponseModel);
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','MODERATOR')")
    @PostMapping("/exercise/create")
    public ResponseEntity<ExerciseResponseModel> createExercise(ExerciseBindingModel exerciseBindingModel, UriComponentsBuilder builder) {
        ExerciseServiceModel exerciseServiceModel = exerciseService
                .create(modelMapper.map(exerciseBindingModel, ExerciseServiceModel.class));
        ExerciseResponseModel exerciseResponseModel = modelMapper
                .map(exerciseServiceModel, ExerciseResponseModel.class);

        ResponseEntity.BodyBuilder created =
                ResponseEntity.created(builder.path("categories/" + exerciseServiceModel.getId()).build().toUri());
        return created.body(exerciseResponseModel);
    }

    @GetMapping(value = "/category/all", produces = "application/json")
    public List<String> getAllGrammarCategories() {
        return grammarCategoryService
                .getAll()
                .stream()
                .map(GrammarCategoryServiceModel::getName)
//                .map(m -> modelMapper.map(m, GrammarNameResponseModel.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<GrammarCategoryResponseModel> getGrammarCategoryContent(@PathVariable String name) {
        GrammarCategoryResponseModel responseModel =
                modelMapper.map(grammarCategoryService.getGrammarCategory(name),
                        GrammarCategoryResponseModel.class);

        return ResponseEntity.ok(responseModel);
    }

    @PreAuthorize(value = "hasAnyRole('ROOT_ADMIN', 'ADMIN', 'MODERATOR')")
    @PostMapping("/add")
    public ResponseEntity<ContentResponseModel> createContent(
            @RequestBody ContentBindingModel model) {
        ContentResponseModel map = modelMapper.map(
                grammarCategoryService
                        .uploadContent(modelMapper.map(model, ContentServiceModel.class)),
                ContentResponseModel.class);

        String path = String.format("/category/%s/%s", map.getCategoryId(), map.getTitle());
        return ResponseEntity.created(URI.create(path)).body(map);
    }

    @GetMapping(value = "/category/{categoryId}/{contentId}")
    public ResponseEntity<ContentResponseModel> getContent(@PathVariable String categoryId, @PathVariable String contentId) {
        ContentResponseModel responseModel =
                modelMapper.map(contentService.getContentByCategoryAndId(categoryId, contentId),
                        ContentResponseModel.class);
        return ResponseEntity.ok(responseModel);
    }

    @PostMapping(value = "/category/{categoryId}/{contentId}")
    public ResponseEntity<CommentResponseModel> addComment(
            @PathVariable String categoryId,
            @PathVariable String contentId,
            @RequestBody CommentBindingModel commentBindingModel,
            UriComponentsBuilder uriComponentsBuilder) {
        CommentServiceModel map = modelMapper.map(commentBindingModel, CommentServiceModel.class);

        CommentServiceModel commentServiceModel = contentService.addCommentToContent(map);

        CommentResponseModel responseModel = modelMapper.map(commentServiceModel, CommentResponseModel.class);

        String path = String.format("/category/%s/%s", categoryId, contentId);

        return ResponseEntity
                .created(uriComponentsBuilder.path(path).build().toUri())
                .body(responseModel);
    }

}