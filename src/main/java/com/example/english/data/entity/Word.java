package com.example.english.data.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static com.example.english.constants.WordConstants.WORD_DEFINITION_IS_REQUIRED;
import static com.example.english.constants.WordConstants.WORD_NAME_IS_REQUIRED;

@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Data
@Table(name = "words")
@AllArgsConstructor
@Builder
public class Word extends BaseEntity {
    @NotBlank(message = WORD_NAME_IS_REQUIRED)
    private String name;
    @NotBlank(message = WORD_DEFINITION_IS_REQUIRED)
    private String definition;
}
