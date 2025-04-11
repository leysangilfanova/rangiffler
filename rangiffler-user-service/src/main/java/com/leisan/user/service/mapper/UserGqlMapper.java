package com.leisan.user.service.mapper;

import com.leisan.user.service.graphql.dto.UserGql;
import com.leisan.user.service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserGqlMapper {
    UserGqlMapper INSTANCE = Mappers.getMapper(UserGqlMapper.class);

    @Mapping(target = "location", ignore = true)
    @Mapping(target = "friendStatus", ignore = true)
    @Mapping(target = "friends", ignore = true)
    @Mapping(target = "incomeInvitations", ignore = true)
    @Mapping(target = "outcomeInvitations", ignore = true)
    @Mapping(target = "firstname", source = "firstName")
    @Mapping(target = "surname", source = "lastName")
    UserGql toGql(User user);
}
