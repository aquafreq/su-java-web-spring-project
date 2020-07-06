package com.example.english.data.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class ServiceModelWord {

    @NotNull
    @NonNull
    private String name;

    @NotNull
    @NonNull
    private String definition;
}
