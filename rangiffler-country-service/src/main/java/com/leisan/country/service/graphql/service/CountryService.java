package com.leisan.country.service.graphql.service;

import com.leisan.country.service.graphql.dto.CountryGql;

import java.util.List;

public interface CountryService {

    CountryGql getCountry(String code);

    List<CountryGql> getCountries();
}
