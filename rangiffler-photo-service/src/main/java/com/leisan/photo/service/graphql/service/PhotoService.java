package com.leisan.photo.service.graphql.service;

import com.leisan.photo.service.graphql.dto.PhotoGql;
import com.leisan.photo.service.graphql.dto.PhotoInputGql;

import java.util.List;

public interface PhotoService {

    PhotoGql getPhoto(String id);

    List<PhotoGql> getPhotos(List<String> userIds,
                             int page,
                             int size);

    Integer countPhotos(List<String> userId);

    boolean deletePhoto(String userId,
                        String id);

    PhotoGql createOrUpdatePhoto(String userId,
                                 PhotoInputGql photoInputGql);
}
