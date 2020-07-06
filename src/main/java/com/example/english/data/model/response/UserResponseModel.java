package com.example.english.data.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UserResponseModel implements Serializable {
    private String id;
    private String username;
    private List<String> authorities;

    @Override
    public String toString() {
        return String.format("%s@%s@%s",
                id, username, String.join("@", authorities));
    }
}
