package com.example.english.data.model.response;


import com.example.english.data.entity.enumerations.UserActivity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
public class UserResponseModel extends RepresentationModel<UserResponseModel> {
    private String id;
    private String username;
    private String email;
    private boolean isEnabled;
    private UserActivity activity;
    private List<RoleResponseModel> authorities;

    @Override
    public String toString() {
        return String.format("%s@%s@%s",
                id, username, authorities
                        .stream()
                        .map(RoleResponseModel::getAuthority)
                        .collect(Collectors.joining("@")));
    }
}
