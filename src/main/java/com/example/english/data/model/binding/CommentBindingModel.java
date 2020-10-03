package com.example.english.data.model.binding;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import static com.example.english.constants.ContentConstants.ENTER_A_VALID_COMMENT;
import static com.example.english.constants.ContentConstants.THE_COMMENT_CANNOT_BE_EMPTY;

@Data
@NoArgsConstructor
public class CommentBindingModel {
    @NotBlank(message = THE_COMMENT_CANNOT_BE_EMPTY)
    @Length(min = 3, max = 255, message = ENTER_A_VALID_COMMENT)
    private String message;
    private String contendId;
    private String categoryId;
    private String userId = "Anonymous";
}
