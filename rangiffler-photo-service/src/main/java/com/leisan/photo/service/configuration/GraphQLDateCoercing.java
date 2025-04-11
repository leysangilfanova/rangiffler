package com.leisan.photo.service.configuration;

import graphql.language.StringValue;
import graphql.schema.Coercing;

import java.time.Instant;

public class GraphQLDateCoercing implements Coercing<Instant, String> {

    @Override
    public String serialize(Object dataFetcherResult) {
        return ((Instant) dataFetcherResult).toString();
    }

    @Override
    public Instant parseValue(Object input) {
        return Instant.parse(input.toString());
    }

    @Override
    public Instant parseLiteral(Object input) {
        if (input instanceof StringValue stringValue) {
            return Instant.parse(stringValue.getValue());
        }
        return null;
    }
}