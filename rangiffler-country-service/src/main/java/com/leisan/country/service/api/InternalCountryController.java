package com.leisan.country.service.api;

import com.leisan.country.service.graphql.dto.CountryGql;
import com.leisan.country.service.graphql.service.CountryService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import lombok.RequiredArgsConstructor;

@Controller(value = "/internal/country")
@RequiredArgsConstructor
public class InternalCountryController {
    private final CountryService countryService;

    @Get(value = "/{code}")
    public CountryGql getCountryByCode(@PathVariable(value = "code") String code) {
        return countryService.getCountry(code);
    }
}
