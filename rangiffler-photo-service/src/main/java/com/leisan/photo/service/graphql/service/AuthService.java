package com.leisan.photo.service.graphql.service;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.utils.SecurityService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class AuthService {
    private final SecurityService securityService;

    public String getCurrentUsername() {
        return securityService.getAuthentication()
                .map(Authentication::getName)
                .orElse("unknown");
    }
}
