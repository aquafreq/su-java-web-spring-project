package com.example.english.data.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Data
@Table(name = "words")
@AllArgsConstructor
@Builder
public class Word extends BaseEntity {
    @NotBlank(message = "Name is required!")
    private String name;
    @NotBlank(message = "Definition is required!")
    private String definition;
}
