package com.leisan.country.service.mapper;

import com.leisan.country.service.graphql.dto.CountryGql;
import com.leisan.country.service.model.Country;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CountryGqlMapper {
    CountryGqlMapper INSTANCE = Mappers.getMapper(CountryGqlMapper.class);

    CountryGql toGql(Country country);
}
