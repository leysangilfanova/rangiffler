package com.leisan.rangiffler.jupiter.annotation.meta;

import com.leisan.rangiffler.jupiter.extension.CreateExtrasUserExtension;
import com.leisan.rangiffler.jupiter.extension.UserExtension;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({
        AllureJunit5.class,
        UserExtension.class,
        CreateExtrasUserExtension.class
})
public @interface GqlTest {
}
