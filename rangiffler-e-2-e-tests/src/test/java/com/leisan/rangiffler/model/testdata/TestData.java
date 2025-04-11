package com.leisan.rangiffler.model.testdata;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nonnull;

public record TestData(@JsonIgnore @Nonnull String password) {

}
