package com.example.english.web.controller;

import com.example.english.data.model.response.RoleResponseModel;
import com.example.english.data.model.response.UserDetailsResponseModel;
import com.example.english.data.model.response.UserResponseModel;
import com.example.english.data.model.service.RoleServiceModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.service.RoleService;
import com.example.english.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final RoleService roleService;

    @PreAuthorize(value = "hasAnyRole('ADMIN','ROOT_ADMIN')")
    @PatchMapping("/user/{id}/add-role/{roleId}")
    public ResponseEntity<UserResponseModel> giveUserRole(@PathVariable String id, @PathVariable String roleId) {

        UserResponseModel userResponseModel =
                modelMapper.map(userService.giveUserRole(id, roleId)
                        , UserResponseModel.class);
        mapUserLinks(filterRole("ROLE_ROOT_ADMIN")).accept(userResponseModel);
        return ResponseEntity.ok(userResponseModel);
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','ROOT_ADMIN')")
    @PatchMapping("/user/{id}/remove-role/{roleId}")
    public ResponseEntity<UserResponseModel> removeUserRole(@PathVariable String id, @PathVariable String roleId) {
        UserResponseModel userResponseModel = modelMapper.map(userService.removeUserRole(id, roleId),
                UserResponseModel.class);

        mapUserLinks(filterRole("ROLE_ROOT_ADMIN")).accept(userResponseModel);
        return ResponseEntity.ok(userResponseModel);
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','ROOT_ADMIN')")
    @GetMapping("/user/all")
    public CollectionModel<EntityModel<UserResponseModel>> getAllUsers() {
        List<String> allRoles = filterRole("ROLE_ROOT_ADMIN");

        List<UserResponseModel> userResponseModels = userService.getAllUsers()
                .stream()
                .map(x -> modelMapper.map(x, UserResponseModel.class))
                .peek(mapUserLinks(allRoles))
                .collect(Collectors.toList());

        return CollectionModel.wrap(userResponseModels);
    }

//    @PreAuthorize(value = "hasAnyRole('ADMIN','ROOT_ADMIN')")
//    @GetMapping(value = "/user/roles", produces = "application/json")
//    public List<String> getUserRoles(String id) {
//        return userService.getUserRoles(id);
//    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','ROOT_ADMIN')")
    @GetMapping(value = "/role/all", produces = "application/json")
    public List<RoleResponseModel> getUserRoles() {
        return roleService.getAllRoles()
                .stream()
                .map(r -> modelMapper.map(r, RoleResponseModel.class))
                .collect(Collectors.toList());
    }


    @PreAuthorize(value = "hasAnyRole('ADMIN','ROOT_ADMIN')")
    @PatchMapping("/user/disable/{id}")
    public ResponseEntity<UserResponseModel> disableUser(@PathVariable String id) {
        UserResponseModel userResponseModel = modelMapper.map(userService.forbidUser(id),
                UserResponseModel.class);

        return ResponseEntity.ok(userResponseModel);
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','ROOT_ADMIN')")
    @PatchMapping("/user/permit/{id}")
    public ResponseEntity<UserResponseModel> permitUser(@PathVariable String id) {
        UserResponseModel userResponseModel = modelMapper.map(userService.permitUser(id),
                UserResponseModel.class);

        return ResponseEntity.ok(userResponseModel);
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'ROOT_ADMIN')")
    @GetMapping("/user/details/{id}")
    public ResponseEntity<UserDetailsResponseModel> getUserDetails(@PathVariable String id) {
        UserServiceModel userServiceModel = userService.getUserDetailsById(id);

        UserDetailsResponseModel map = modelMapper.map(userServiceModel, UserDetailsResponseModel.class);

        map.setAuthorities(userServiceModel
                .getAuthorities()
                .stream()
                .map(RoleServiceModel::getAuthority)
                .map(x -> x.replace("ROLE_", ""))
                .collect(Collectors.joining(", ")));

        map.setIsEnabled(userServiceModel.isEnabled());
        map.setRegistrationDate(userServiceModel.getRegistrationDate().toString()
                .replace("T", " "));
        return ResponseEntity.ok(map);
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN','ROOT_ADMIN')")
    @GetMapping(value = "/user/{id}", produces = "application/hal+json")
    public ResponseEntity<UserResponseModel> getUser(@PathVariable String id) {
        UserResponseModel responseModel =
                modelMapper.map(userService.getUserById(id), UserResponseModel.class);

        List<String> filteredRoles = filterRole("ROLE_ROOT_ADMIN");
        mapUserLinks(filteredRoles).accept(responseModel);

        return ResponseEntity.ok(responseModel);
    }

    private List<String> filterRole(String filterRole) {
        return getAllRoleIds()
                .stream()
                .filter(r -> !r.getAuthority().equals(filterRole))
                .map(RoleServiceModel::getId)
                .collect(Collectors.toList());
    }

    //thin controllers
    private Consumer<UserResponseModel> mapUserLinks(List<String> allRoles) {
        return u -> {
            String id = u.getId();

            Link getUserInfo = linkTo(methodOn(UserController.class)
                    .getUserProfile(id)).withSelfRel();

            Link disable = linkTo(methodOn(AdminUserController.class)
                    .disableUser(id)).withRel("disable-user");

            Link permit = linkTo(methodOn(AdminUserController.class)
                    .permitUser(id)).withRel("permit-user");

            List<String> userRoles = u.getAuthorities()
                    .stream()
                    .map(RoleResponseModel::getId)
                    .collect(Collectors.toList());

            List<Link> rolesToGive = allRoles
                    .stream()
                    .filter(r -> !userRoles.contains(r))
                    .map(r -> linkTo(methodOn(AdminUserController.class)
                            .giveUserRole(u.getId(), r)).withRel("give-user-role"))
                    .collect(Collectors.toList());

            List<Link> rolesToRemove = allRoles
                    .stream()
                    .filter(userRoles::contains)
                    .map(r -> linkTo(methodOn(AdminUserController.class)
                            .removeUserRole(u.getId(), r)).withRel("remove-user-role"))
                    .collect(Collectors.toList());


            u.add(disable, permit, getUserInfo);
            u.add(rolesToGive);
            u.add(rolesToRemove);
        };
    }

    private List<RoleServiceModel> getAllRoleIds() {
        return roleService.getAllRoles();
    }
}
