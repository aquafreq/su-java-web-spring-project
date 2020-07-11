package com.example.english.web.controller;

import com.example.english.data.model.response.RoleResponseModel;
import com.example.english.data.model.response.UserResponseModel;
import com.example.english.data.model.service.RoleServiceModel;
import com.example.english.service.RoleService;
import com.example.english.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminUserController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final RoleService roleService;

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PatchMapping("/user/{id}/add-role/{roleId}")
    public ResponseEntity<UserResponseModel> giveUserRole(@PathVariable String id, @PathVariable String roleId) {

        UserResponseModel userResponseModel =
                modelMapper.map(userService.giveUserRole(id, roleId)
                        , UserResponseModel.class);

        return ResponseEntity.ok(userResponseModel);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseModel> getUser(@PathVariable String id) {
        UserResponseModel responseModel =
                modelMapper.map(userService.getUserById(id),
                        UserResponseModel.class);

        return ResponseEntity.ok(responseModel);
    }

    @GetMapping("/user/all")
    public CollectionModel<EntityModel<UserResponseModel>> getAllUsers() {
        List<String> allRoles = roleService.getAllRoles()
                .stream()
                .map(RoleServiceModel::getId)
                .collect(Collectors.toList());

        List<UserResponseModel> userResponseModels = userService.getAllUsers()
                .stream()
                .map(x -> modelMapper.map(x, UserResponseModel.class))
                .peek(mapLinks(allRoles))
                .collect(Collectors.toList());

        return CollectionModel.wrap(userResponseModels);
    }

    private Consumer<UserResponseModel> mapLinks(List<String> allRoles) {
        return u -> {
            String id = u.getId();

            Link getUserInfo = linkTo(methodOn(AdminUserController.class)
                    .getUser(id)).withSelfRel();

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

    @GetMapping("/user/roles")
    public List<String> getUserRoles(String id) {
        return userService.getUserRoles(id);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PatchMapping("/user/{id}/remove-role/{roleId}")
    public ResponseEntity<UserResponseModel> removeUserRole(@PathVariable String id, @PathVariable String roleId) {
        UserResponseModel userResponseModel = modelMapper.map(userService.removeUserRole(roleId, id),
                UserResponseModel.class);

        return ResponseEntity.ok(userResponseModel);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PatchMapping("/user/disable/{id}")
    public ResponseEntity<UserResponseModel> disableUser(@PathVariable String id) {
        UserResponseModel userResponseModel = modelMapper.map(userService.forbidUser(id),
                UserResponseModel.class);

        return ResponseEntity.ok(userResponseModel);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PatchMapping("/user/permit/{id}")
    public ResponseEntity<UserResponseModel> permitUser(@PathVariable String id) {
        UserResponseModel userResponseModel = modelMapper.map(userService.permitUser(id),
                UserResponseModel.class);

        return ResponseEntity.ok(userResponseModel);
    }
}
