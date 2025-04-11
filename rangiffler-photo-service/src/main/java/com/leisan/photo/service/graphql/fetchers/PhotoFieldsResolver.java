package com.leisan.photo.service.graphql.fetchers;

import com.leisan.photo.service.graphql.dto.CountryDto;
import com.leisan.photo.service.graphql.dto.LikesGql;
import com.leisan.photo.service.graphql.dto.PhotoGql;
import com.leisan.photo.service.graphql.service.CountryService;
import com.leisan.photo.service.graphql.service.LikeService;
import graphql.kickstart.tools.GraphQLResolver;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@Singleton
@RequiredArgsConstructor
public class PhotoFieldsResolver implements GraphQLResolver<PhotoGql> {
    private final LikeService likeService;
    private final CountryService countryService;

    public LikesGql getLikes(PhotoGql photoGql) {
        var likes = likeService.getPhotoLikes(photoGql.id());
        return new LikesGql(likes.size(), likes);
    }

    public CompletableFuture<CountryDto> getCountry(PhotoGql photoGql) {
        return countryService.getCountry(photoGql.countryCode()).toFuture();
    }
}
