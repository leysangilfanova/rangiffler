package com.leisan.user.service.configuration;

import graphql.GraphQL;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLScalarType;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.io.ResourceResolver;
import jakarta.inject.Singleton;

import java.util.List;

@Factory
public class GraphqlConfiguration {
    @Bean
    @Singleton
    public GraphQL graphQL(ResourceResolver resourceResolver,
                           List<GraphQLResolver<?>> resolverList) {

        SchemaParser schemaParser = SchemaParser.newParser()
                .file("graphql/schema.graphqls")
                .resolvers(resolverList)
                .scalars(
                        GraphQLScalarType.newScalar()
                                .name("Date")
                                .coercing(new GraphQLDateCoercing())
                                .build()
                )
                .build();

        return GraphQL.newGraphQL(schemaParser.makeExecutableSchema()).build();
    }

}
