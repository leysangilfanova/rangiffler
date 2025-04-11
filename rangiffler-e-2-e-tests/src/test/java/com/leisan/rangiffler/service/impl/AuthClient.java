package com.leisan.rangiffler.service.impl;

import com.leisan.rangiffler.jupiter.extension.AuthApiClientExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(AuthApiClientExtension.class)
public @interface AuthClient {}
