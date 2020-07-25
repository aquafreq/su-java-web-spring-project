package com.example.english.config;

import com.example.english.data.entity.Comment;
import com.example.english.data.entity.Content;
import com.example.english.data.entity.Role;
import com.example.english.data.entity.enumerations.LevelExperience;
import com.example.english.data.entity.enumerations.LevelOfLanguage;
import com.example.english.data.model.binding.CommentBindingModel;
import com.example.english.data.model.binding.ContentBindingModel;
import com.example.english.data.model.binding.UserProfileBindingModel;
import com.example.english.data.model.service.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        PropertyMap<CommentBindingModel, CommentServiceModel> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setId(null);
                map().setCategoryId(source.getCategoryId());
                map().setContendId(source.getContendId());
                map().setMessage(source.getMessage());
                map().setUserId(source.getUserId());
            }
        };

        PropertyMap<ContentBindingModel, ContentServiceModel> contentBindingModelContentServiceModelPropertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setId(null);
                map().setComments(new ArrayList<>());
                map().setTitle(source.getTitle());
                map().setCreated(source.getCreated());
                map().setDescription(source.getDescription());
            }
        };


//        TypeMap<UserProfileBindingModel, UserProfileServiceModel> typeMap =
//                modelMapper.createTypeMap(UserProfileBindingModel.class, UserProfileServiceModel.class);

//        typeMap.addMappings(mapper -> {
//            mapper
//                    .when(context -> context.getSource() != null).map(src -> Arrays.stream(src.getHobbies().split(", ")).collect(Collectors.toList())
//                    , (destination, value) -> destination.setHobbies(((List<String>) value)));
//            mapper
//                    .when(context -> context.getSource() != null)
//                    .map(UserProfileBindingModel::getLevelExperience,
//                            (destination, value) -> destination.setLevelExperience(LevelExperience.valueOf(value.toString())));
//            mapper
//                    .when(context -> context.getSource() != null)
//                    .map(UserProfileBindingModel::getLevelOfLanguage,
//                            (destination, value) -> destination.setLevelOfLanguage(LevelOfLanguage.valueOf(value.toString())));
//            mapper
//                    .when(context -> context.getSource() != null)
//                    .map(UserProfileBindingModel::getBirthDate,
//                            (destination, value) -> destination.setBirthDate(((LocalDate) value)));
//            mapper
//                    .when(context -> context.getSource() != null)
//                    .map(UserProfileBindingModel::getUsername,
//                            (destination, value) -> destination.setUsername(value.toString()));
//            mapper
//                    .when(context -> context.getSource() != null)
//                    .map(UserProfileBindingModel::getEmail,
//                            (destination, value) -> destination.setEmail(((String) value)));
//            mapper
//                    .when(context -> context.getSource() != null)
//                    .map(UserProfileBindingModel::getNationality,
//                            (destination, value) -> destination.setNationality(value.toString()));
//        });

        modelMapper.addMappings(propertyMap);
        modelMapper.addMappings(contentBindingModelContentServiceModelPropertyMap);
        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }
}
