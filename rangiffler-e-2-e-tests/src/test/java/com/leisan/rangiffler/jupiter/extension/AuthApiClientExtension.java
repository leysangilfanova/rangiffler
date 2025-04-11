package com.leisan.rangiffler.jupiter.extension;

import com.leisan.rangiffler.service.impl.AuthApiClient;
import com.leisan.rangiffler.service.impl.AuthClient;
import org.junit.jupiter.api.extension.*;
import java.util.HashMap;
import java.util.Map;

public class AuthApiClientExtension implements ParameterResolver, AfterEachCallback {
    private final Map<String, AuthApiClient> clients = new HashMap<>();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().isAnnotationPresent(AuthClient.class)
                && parameterContext.getParameter().getType().equals(AuthApiClient.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        String testId = extensionContext.getUniqueId();
        AuthApiClient client = new AuthApiClient();
        clients.put(testId, client);
        return client;
    }

    @Override
    public void afterEach(ExtensionContext context) {
        String testId = context.getUniqueId();
        AuthApiClient client = clients.remove(testId);
        if (client != null) {
            client.cleanup();
        }
    }
}