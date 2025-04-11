package com.leisan.graphql.common.test;

import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class GraphqlAssert extends AbstractAssert<GraphqlAssert, String> {
    public GraphqlAssert(String s) {
        super(s, GraphqlAssert.class);
        isNotNull();
    }

    public <T> GraphqlAssert assertEqualsByJsonPath(String jsonPath,
                                                    T expected) {
        super.satisfies(actual -> {
            var actualValue = JsonPath.read(actual, "$.data." + jsonPath);
            Assertions.assertThat(actualValue).isEqualTo(expected);
        });
        return this;
    }

    public <T> GraphqlAssert extracting(String path) {
        return new GraphqlAssert(JsonPath.read(actual, path));
    }
}
