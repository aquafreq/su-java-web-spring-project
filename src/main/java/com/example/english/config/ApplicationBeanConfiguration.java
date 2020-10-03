package com.example.english.config;

import com.example.english.data.model.binding.CommentBindingModel;
import com.example.english.data.model.binding.ContentBindingModel;
import com.example.english.data.model.service.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Properties;

@Configuration
@Slf4j
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

//    @Bean
//    @Profile("test")
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
//        dataSource.setUsername("sa");
//        dataSource.setPassword("sa");
//        return dataSource;
//    }

    @Bean
    @Profile("!test")
    public JavaMailSender getJavaMailSender() throws MessagingException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.abv.bg");
        mailSender.setPort(465);

        mailSender.setUsername("hello-english@abv.bg");
        mailSender.setPassword("asdASD123");

//        mailSender.setHost(SERVER);
//        mailSender.setPort(Integer.parseInt(PORT));


//        mailSender.setUsername(EMAIL);
//        mailSender.setPassword(EMAIL_PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.smtp.host", SERVER);
//        props.put("mail.smtp.port",PORT);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.ssl.enable", "true");

//        Session session = Session.getInstance(props,
//                new Authenticator() {
//                    @Override
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(mailSender.getUsername(),
//                                mailSender.getPassword());
//                    }
//                });

//        SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

        //Connect to the server using credentials

//        t.connect(SERVER, mailSender.getUsername(), mailSender.getPassword());

        return mailSender;
    }


    //TODO ENABLE WHEN DEPLOY
//    @Value("${spring.datasource.url}")
//    private String dbUrl;
//
//    @Bean
//    public DataSource dataSource() {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(dbUrl);
//        return new HikariDataSource(config);
//    }
}
