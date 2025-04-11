package com.leisan.rangiffler.jupiter.annotation;

import com.leisan.rangiffler.jupiter.extension.UserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(UserExtension.class)
public @interface CreateUser {
  String username() default "";

  String password() default "";

  Friend[] friends() default {};
}
