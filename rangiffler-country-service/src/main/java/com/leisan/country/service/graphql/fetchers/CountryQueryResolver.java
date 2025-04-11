package com.leisan.country.service.graphql.fetchers;

import com.leisan.country.service.graphql.dto.CountryGql;
import com.leisan.country.service.graphql.service.CountryService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class CountryQueryResolver implements GraphQLQueryResolver {
    private final CountryService countryService;

    public CountryGql country(String code) {
        return countryService.getCountry(code);
    }

    public List<CountryGql> countries() {
        return countryService.getCountries();
    }

}
