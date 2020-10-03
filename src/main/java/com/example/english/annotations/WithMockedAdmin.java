package com.example.english.annotations;


import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(value="fizz",roles= {"ADMIN","ROOT_ADMIN"})
public @interface WithMockedAdmin {
}
