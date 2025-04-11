package com.leisan.photo.service.mapper;

import com.leisan.photo.service.graphql.dto.PhotoGql;
import com.leisan.photo.service.model.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PhotoMapper {
    PhotoMapper INSTANCE = Mappers.getMapper(PhotoMapper.class);

    PhotoGql toGql(Photo photo);

    List<PhotoGql> toGqlAsList(List<Photo> photos);
}
