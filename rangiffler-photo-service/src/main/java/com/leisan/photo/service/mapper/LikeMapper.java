package com.leisan.photo.service.mapper;

import com.leisan.photo.service.graphql.dto.LikeGql;
import com.leisan.photo.service.model.Likes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LikeMapper {
    LikeMapper INSTANCE = Mappers.getMapper(LikeMapper.class);

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "username", constant = "userId")
    LikeGql toGql(Likes like);

    List<LikeGql> toGqlAsList(List<Likes> likes);
}
