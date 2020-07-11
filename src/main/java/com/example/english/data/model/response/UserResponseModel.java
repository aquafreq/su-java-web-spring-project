package com.example.english.data.model.response;


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
    private boolean isEnabled;
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
