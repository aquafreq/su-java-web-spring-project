package com.example.english.data.entity;

import lombok.*;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class Log extends BaseEntity{
    private String username;
    private String userId;
    private String url;
    private String method;
    private LocalDateTime occurrence;
}
