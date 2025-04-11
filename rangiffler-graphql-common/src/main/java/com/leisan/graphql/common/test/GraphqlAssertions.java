package com.leisan.graphql.common.test;

import org.assertj.core.api.Assertions;

public class GraphqlAssertions {
    public static GraphqlAssert assertGraphqlRs(String graphqlRs) {
        Assertions.assertThat(graphqlRs).isNotNull()
                .isNotNull();
        return new GraphqlAssert(graphqlRs);
    }
}
