package com.example.english.web.controller;

import com.example.english.annotations.Validate;
import com.example.english.data.entity.enumerations.LevelOfLanguage;
import com.example.english.data.model.binding.CommentBindingModel;
import com.example.english.data.model.binding.ContentBindingModel;
import com.example.english.data.model.binding.GrammarCategoryBindingModel;
import com.example.english.data.model.response.*;
import com.example.english.data.model.service.CommentServiceModel;
import com.example.english.data.model.service.ContentServiceModel;
import com.example.english.data.model.service.GrammarCategoryServiceModel;
import com.example.english.service.ContentService;
import com.example.english.service.GrammarCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/content")
@RequiredArgsConstructor
public class ContentController {
    private final GrammarCategoryService grammarCategoryService;
    private final ContentService contentService;
    private final ModelMapper modelMapper;

    @Validate
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR', 'ROOT_ADMIN')")
    @PostMapping("/category/create")
    public ResponseEntity<GrammarCategoryResponseModel> createCategory(@RequestBody GrammarCategoryBindingModel grammarCategoryBindingModel, UriComponentsBuilder builder) {
        GrammarCategoryResponseModel categoryResponseModel =
                modelMapper.map(grammarCategoryService.create(
                        modelMapper.map(grammarCategoryBindingModel, GrammarCategoryServiceModel.class)),
                        GrammarCategoryResponseModel.class);

        ResponseEntity.BodyBuilder created = ResponseEntity.created(builder.path("content/create/category/" + categoryResponseModel.getId()).build().toUri());
        return created.body(categoryResponseModel);
    }

    @GetMapping(value = "/category/all-categories", produces = "application/json")
    public ResponseEntity<List<GrammarCategoryResponseModel>> getAllGrammarCategories() {
        return ResponseEntity.ok(grammarCategoryService
                .getAll()
                .stream()
                .map(m -> modelMapper.map(m, GrammarCategoryResponseModel.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<GrammarCategoryResponseModel> getGrammarCategoryContent(@PathVariable String name) {
        GrammarCategoryResponseModel responseModel =
                modelMapper.map(grammarCategoryService.getGrammarCategory(name),
                        GrammarCategoryResponseModel.class);

        return ResponseEntity.ok(responseModel);
    }

    @Validate
    @PreAuthorize(value = "hasAnyRole('ROOT_ADMIN', 'ADMIN', 'MODERATOR')")
    @PostMapping("/create")
    public ResponseEntity<ContentResponseModel> createContent(
            @RequestBody ContentBindingModel model) {
        ContentServiceModel map = modelMapper.map(model, ContentServiceModel.class);
        ContentServiceModel contentServiceModel = grammarCategoryService.uploadContent(map);

        ContentResponseModel responseModel = modelMapper.map(contentServiceModel, ContentResponseModel.class);

        String path = mapCreatedURI(responseModel);
        return ResponseEntity.created(URI.create(path)).body(responseModel);
    }

    private String mapCreatedURI(ContentResponseModel responseModel) {
        return String.format("/category/%s/%s",
                responseModel.mapValue(responseModel.getCategoryName()),
                responseModel.mapValue(responseModel.getTitle()));
    }

    @GetMapping(value = "/category/{categoryId}/{contentId}")
    public ResponseEntity<ContentResponseModel> getContent(@PathVariable String categoryId, @PathVariable String contentId) {
        ContentServiceModel contentByCategoryAndId = contentService.getContentByCategoryAndId(categoryId, contentId);

        ContentResponseModel responseModel =
                modelMapper.map(contentByCategoryAndId, ContentResponseModel.class);

        return ResponseEntity.ok(responseModel);
    }

    @Validate
    @PostMapping(value = "/category/{category}/{content}")
    public ResponseEntity<CommentResponseModel> addComment(
            @PathVariable String category,
            @PathVariable String content,
            @RequestBody CommentBindingModel commentBindingModel,
            UriComponentsBuilder uriComponentsBuilder) {
        CommentServiceModel map = modelMapper.map(commentBindingModel, CommentServiceModel.class);

        CommentServiceModel commentServiceModel = contentService.addCommentToContent(map);

        CommentResponseModel responseModel = modelMapper.map(commentServiceModel, CommentResponseModel.class);

        String path = String.format("/category/%s/%s", category, content);

        return ResponseEntity
                .created(uriComponentsBuilder.path(path).build().toUri())
                .body(responseModel);
    }

    @GetMapping("/category/levels")
    @Cacheable("levelsOfLanguage")
    public List<String> getLevelOfLanguages() {
        return Arrays.stream(LevelOfLanguage.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
