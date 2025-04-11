package com.leisan.country.service.graphql.service;

import com.leisan.country.service.graphql.dto.CountryGql;
import com.leisan.country.service.mapper.CountryGqlMapper;
import com.leisan.country.service.repository.CountryRepository;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public CountryGql getCountry(String code) {
        return countryRepository.findByCode(code)
                .map(CountryGqlMapper.INSTANCE::toGql)
                .orElseThrow();
    }

    @Override
    public List<CountryGql> getCountries() {
        return countryRepository.findAll()
                .stream()
                .map(CountryGqlMapper.INSTANCE::toGql)
                .toList();
    }
}
