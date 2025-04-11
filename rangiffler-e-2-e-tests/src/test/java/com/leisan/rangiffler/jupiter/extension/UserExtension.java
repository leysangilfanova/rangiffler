package com.leisan.rangiffler.jupiter.extension;

import com.leisan.rangiffler.jupiter.annotation.CreateUser;
import com.leisan.rangiffler.service.impl.AuthApiClient;
import com.leisan.rangiffler.model.testdata.TestData;
import com.leisan.rangiffler.model.testdata.TestUser;
import com.leisan.rangiffler.utils.RandomDataUtils;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    public static final String defaultPassword = "12345";

    @Override
    @Step("Создать пользователя")
    public void beforeEach(ExtensionContext context) {
        AuthApiClient authApiClient = new AuthApiClient();

        var userParameters = AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                CreateUser.class
        );

        if (userParameters.isPresent()) {
            String username = RandomDataUtils.randomUsername();

            try {
                var response = authApiClient.register(username, defaultPassword, defaultPassword);
                if (!response.isSuccessful()) {
                    throw new RuntimeException("Не удалось зарегистрировать пользователя через API. Код: " + response.code());
                }
            } catch (Exception e) {
                throw new RuntimeException("Ошибка при регистрации пользователя через API", e);
            }

            TestUser testUser = TestUser.builder()
                    .username(username)
                    .testData(new TestData(defaultPassword))
                    .build();

            context.getStore(NAMESPACE).put(context.getUniqueId(), testUser);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType().isAssignableFrom(TestUser.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), TestUser.class);
    }

    public static TestUser getUserJson() {
        final ExtensionContext context = TestMethodContextExtension.context();
        return context.getStore(NAMESPACE).get(context.getUniqueId(), TestUser.class);
    }
}
