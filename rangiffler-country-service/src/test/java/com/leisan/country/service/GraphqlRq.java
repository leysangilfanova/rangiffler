package com.leisan.country.service;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Serdeable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GraphqlRq {
    private String query;
    private String operationName;
    private Map<String, Object> variables;
}
