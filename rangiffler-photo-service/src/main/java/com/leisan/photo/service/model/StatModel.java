package com.leisan.photo.service.model;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Introspected
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatModel {
    private Integer photoCount;
    private String countryCode;
}
