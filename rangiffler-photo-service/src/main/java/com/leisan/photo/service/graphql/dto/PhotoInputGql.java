package com.leisan.photo.service.graphql.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record PhotoInputGql(String id,
                            String src,
                            CountryInputGql country,
                            String description,
                            LikeInputGql like) {
}
