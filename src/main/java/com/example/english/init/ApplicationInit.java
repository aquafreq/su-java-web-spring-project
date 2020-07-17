package com.example.english.init;

import com.example.english.data.entity.GrammarCategory;
import com.example.english.data.entity.Role;
import com.example.english.data.entity.User;
import com.example.english.data.entity.enumerations.RoleEnum;
import com.example.english.data.model.binding.UserRegisterBindingModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.data.repository.RoleRepository;
import com.example.english.data.repository.UserRepository;
import com.example.english.service.GrammarCategoryService;
import com.example.english.service.RoleService;
import com.example.english.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ApplicationInit implements CommandLineRunner {
    private final RoleService roleService;
    private final UserService userService;
    private final GrammarCategoryService grammarCategoryService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleService.getRoleCount() == 0) {
            roleService.seedRoles(
                    Arrays.stream(RoleEnum.values())
                            .map(r -> new Role(r.name())).collect(Collectors.toList())
            );
        }

        if (userService.getCount() == 0) {
            users().forEach(userService::register);
            User zxc = userService.getUserByName("zxc");
            Role role_moderator = roleService.getRoleByName("ROLE_MODERATOR");
            zxc.getAuthorities().add(role_moderator);
            userService.updateUser(zxc);
        }

        if (grammarCategoryService.getCount() == 0) {
            grammarCategoryService.seedCategories(grammarCategories());
        }
    }

    private static List<UserServiceModel> users() {
        return List.of(
                new UserServiceModel("fizz", "fizz", "fizz@fizz.fizz"),
                new UserServiceModel("fizz2", "fizz", "fizz22@fizz.fizz"),
                new UserServiceModel("fizz22", "fizz", "fizz222@fizz.fizz"),
                new UserServiceModel("wow", "wow", "wow@wow.wow"),
                new UserServiceModel("zxc", "zxc", "zxc@zxc.zxc")
        );
    }

    private static GrammarCategory[] grammarCategories() {
        return new GrammarCategory[]{
                new GrammarCategory("Tenses"),
                new GrammarCategory("Adjectives and adverbs"),
                new GrammarCategory("Conditionals"),
                new GrammarCategory("Passive and active voice"),
                new GrammarCategory("Reported Speech"),
                new GrammarCategory("Relative clauses"),
        };
    }


}
