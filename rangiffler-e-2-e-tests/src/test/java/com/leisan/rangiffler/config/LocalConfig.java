package com.leisan.rangiffler.config;

import javax.annotation.Nonnull;

enum LocalConfig implements Config {
  INSTANCE;

  @Nonnull
  @Override
  public String frontUrl() {
    return "http://localhost:4000";
  }

  @Nonnull
  @Override
  public String authUrl() {
    return "http://localhost:9000";
  }

  @Nonnull
  @Override
  public String redirectUrl() {
    return "http://localhost";
  }
}
