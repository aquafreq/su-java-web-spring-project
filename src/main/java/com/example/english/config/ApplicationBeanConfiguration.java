package com.example.english.config;

import com.example.english.data.entity.Comment;
import com.example.english.data.entity.Role;
import com.example.english.data.model.binding.CommentBindingModel;
import com.example.english.data.model.service.CommentServiceModel;
import com.example.english.data.model.service.UserServiceModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<Role, String> toAuthorityString = new Converter<Role, String>() {
            public String convert(MappingContext<Role, String> context) {
                return context.getSource() == null ? null : context.getSource()
                        .getAuthority().substring("ROLE_".length());
            }
        };

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

//          доеснт' ворк
//        Converter<Role, String> toAuthorityString =
//                context -> context.getSource() == null ? null : context.getSource()
//                .getAuthority().substring("ROLE_".length());

        modelMapper.addConverter(toAuthorityString);
        modelMapper.addMappings(propertyMap);
        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }
}
