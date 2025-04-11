package com.leisan.rangiffler.config;

import javax.annotation.Nonnull;

enum DockerConfig implements Config {
  INSTANCE;

  @Nonnull
  @Override
  public String frontUrl() {
    return "http://frontend.niffler.dc/";
  }

  @Nonnull
  @Override
  public String authUrl() {
    return "http://auth.niffler.dc:9000/";
  }

  @Nonnull
  @Override
  public String redirectUrl() {
    return "http://localhost";
  }
}
