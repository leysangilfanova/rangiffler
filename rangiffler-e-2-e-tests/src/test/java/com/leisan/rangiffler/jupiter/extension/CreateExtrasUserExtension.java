package com.leisan.rangiffler.jupiter.extension;

import com.leisan.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.leisan.rangiffler.jupiter.annotation.CreateUser;
import com.leisan.rangiffler.jupiter.annotation.Extras;
import com.leisan.rangiffler.service.impl.AuthApiClient;
import com.leisan.rangiffler.model.testdata.TestData;
import com.leisan.rangiffler.model.testdata.TestUser;
import com.leisan.rangiffler.utils.RandomDataUtils;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreateExtrasUserExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(CreateExtrasUserExtension.class);
    private static final String DEFAULT_PASSWORD = "12345";
    private final AuthApiClient authApiClient = new AuthApiClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        var usersParameters = extractUsersForTest(context);
        var createdUsers = usersParameters.stream()
                .map(params -> registerUser())
                .collect(Collectors.toList());

        context.getStore(NAMESPACE).put(context.getUniqueId(), createdUsers);
    }

    @Step("Зарегистрировать дополнительных пользователей")
    private TestUser registerUser() {
        String username = RandomDataUtils.randomUsername();
        try {
            Response response = authApiClient.register(username, DEFAULT_PASSWORD, DEFAULT_PASSWORD);
            if (!response.isSuccessful()) {
                throw new RuntimeException("Не удалось зарегистрировать пользователя: " + response.code());
            }

            return TestUser.builder()
                    .username(username)
                    .testData(new TestData(DEFAULT_PASSWORD))
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при регистрации пользователя через API", e);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
    }

    @Override
    public boolean supportsParameter(ParameterContext paramCtx, ExtensionContext ctx)
            throws ParameterResolutionException {
        return paramCtx.getParameter().getType().isAssignableFrom(TestUser[].class)
                && ctx.getRequiredTestMethod().isAnnotationPresent(CreateExtrasUsers.class)
                && paramCtx.getParameter().isAnnotationPresent(Extras.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object resolveParameter(ParameterContext paramCtx, ExtensionContext ctx)
            throws ParameterResolutionException {
        List<TestUser> users = ctx.getStore(NAMESPACE).get(ctx.getUniqueId(), List.class);
        return users.toArray(TestUser[]::new);
    }

    private List<CreateUser> extractUsersForTest(ExtensionContext context) {
        List<CreateUser> users = new ArrayList<>();
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), CreateExtrasUsers.class).ifPresent(
                createUsers -> users.addAll(Arrays.asList(createUsers.value()))
        );
        return users;
    }
}
