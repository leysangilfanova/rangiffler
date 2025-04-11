package com.leisan.rangiffler.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leisan.rangiffler.api.AuthApi;
import com.leisan.rangiffler.api.core.RestClient;
import com.leisan.rangiffler.config.Config;
import com.leisan.rangiffler.api.core.ThreadSafeCookieStore;
import com.leisan.rangiffler.utils.OAuthUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;

@Slf4j
public class AuthApiClient extends RestClient {

    private static final Config CFG = Config.getInstance();
    private static final String CLIENT_ID = "client";
    private static final String RESPONSE_TYPE = "code";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String SCOPE = "openid";
    private static final String CODE_CHALLENGE_METHOD = "S256";
    private static final String REDIRECT_URI = buildRedirectUri();

    private final AuthApi authApi;
    private final ThreadLocal<String> codeVerifierHolder = new ThreadLocal<>();

    public AuthApiClient() {
        super(CFG.authUrl(), true, ScalarsConverterFactory.create(), HEADERS);
        this.authApi = create(AuthApi.class);
    }

    /**
     * Выполняет аутентификацию пользователя
     * @param username имя пользователя
     * @param password пароль
     * @return JWT токен
     */
    @SneakyThrows
    public String doLogin(String username, String password) {
        ThreadSafeCookieStore.INSTANCE.clear();

        String codeVerifier = OAuthUtils.generateCodeVerifier();
        codeVerifierHolder.set(codeVerifier);
        String codeChallenge = OAuthUtils.generateCodeChallenge(codeVerifier);

        log.debug("Начало аутентификации для {}: verifier={}, challenge={}",
                username, codeVerifier, codeChallenge);

        authorize(codeChallenge);
        String code = executeLoginFlow(username, password);
        return exchangeCodeForToken(code, codeVerifier);
    }

    /**
     * Регистрирует нового пользователя
     */
    @SneakyThrows
    public Response<Void> register(String username, String password, String confirmPassword) {
        authApi.requestRegisterForm().execute();
        String csrfToken = ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN");
        return authApi.register(username, password, confirmPassword, csrfToken).execute();
    }

    /**
     * Очищает ресурсы после использования
     */
    public void cleanup() {
        codeVerifierHolder.remove();
        ThreadSafeCookieStore.INSTANCE.clear();
        log.debug("Ресурсы клиента очищены");
    }

    @SneakyThrows
    private void authorize(String codeChallenge) {
        Response<Void> response = authApi.authorize(
                RESPONSE_TYPE,
                CLIENT_ID,
                SCOPE,
                REDIRECT_URI,
                codeChallenge,
                CODE_CHALLENGE_METHOD
        ).execute();

        if (!response.isSuccessful()) {
            throw new RuntimeException("Ошибка авторизации: " + response.code());
        }
    }

    @SneakyThrows
    private String executeLoginFlow(String username, String password) {
        authApi.requestLoginForm().execute();

        String xsrfToken = ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN");
        log.debug("Получен XSRF-TOKEN для {}: {}", username, xsrfToken);

        Response<String> loginResponse = authApi.login(username, password, xsrfToken).execute();
        validateLoginResponse(loginResponse);

        return extractAuthorizationCode(loginResponse);
    }

    private void validateLoginResponse(Response<String> response) {
        if (!response.isSuccessful()) {
            throw new RuntimeException("Ошибка входа: " + response.code());
        }

        if (response.body() != null && response.body().contains("<html>")) {
            throw new RuntimeException("Сервер вернул HTML страницу вместо кода авторизации");
        }
    }

    @SneakyThrows
    private String exchangeCodeForToken(String code, String codeVerifier) {
        log.debug("Обмен кода {} на токен с verifier={}", code, codeVerifier);

        Response<String> tokenResponse = authApi.token(
                CLIENT_ID,
                REDIRECT_URI,
                GRANT_TYPE,
                code,
                codeVerifier
        ).execute();

        if (!tokenResponse.isSuccessful() || tokenResponse.body() == null) {
            String error = tokenResponse.errorBody() != null ?
                    tokenResponse.errorBody().string() : "empty body";
            throw new RuntimeException("Ошибка получения токена: " + error);
        }

        return parseTokenFromResponse(tokenResponse.body());
    }

    private String parseTokenFromResponse(String responseBody) throws Exception {
        return new ObjectMapper()
                .readTree(responseBody.getBytes(StandardCharsets.UTF_8))
                .get("id_token")
                .asText();
    }

    private static String extractAuthorizationCode(Response<String> response) {
        String url = response.raw().request().url().toString();
        int codeIndex = url.indexOf("code=");

        if (codeIndex == -1) {
            throw new RuntimeException("Код авторизации не найден в URL: " + url);
        }

        return url.substring(codeIndex + 5);
    }

    private static String buildRedirectUri() {
        try {
            return new URI(CFG.redirectUrl())
                    .resolve("/authorized")
                    .toString();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Некорректный redirect URL", e);
        }
    }
}