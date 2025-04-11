package com.leisan.photo.service.graphql.service;

import com.leisan.photo.service.graphql.dto.LikeGql;
import com.leisan.photo.service.graphql.dto.PhotoInputGql;

import java.util.List;

public interface LikeService {
    List<LikeGql> getPhotoLikes(String photoId);

    void addLike(String username,
                 PhotoInputGql photoInputGql);
}
