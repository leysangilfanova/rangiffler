package com.leisan.photo.service.graphql.fetchers;

import com.leisan.photo.service.graphql.dto.PhotoGql;
import com.leisan.photo.service.graphql.dto.PhotoInputGql;
import com.leisan.photo.service.graphql.service.AuthService;
import com.leisan.photo.service.graphql.service.LikeService;
import com.leisan.photo.service.graphql.service.PhotoService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@ExecuteOn(TaskExecutors.BLOCKING)
@Singleton
@RequiredArgsConstructor
public class PhotoMutationResolver implements GraphQLMutationResolver {
    private final PhotoService photoService;
    private final LikeService likeService;
    private final AuthService authService;

    public PhotoGql photo(PhotoInputGql photoInputGql) {
        var userId = authService.getCurrentUsername();
        String photoId;
        if (photoInputGql.like() != null) {
            likeService.addLike(userId, photoInputGql);
            photoId = photoInputGql.id();
        } else {
            photoId = photoService.createOrUpdatePhoto(userId, photoInputGql).id();
        }
        return photoService.getPhoto(photoId);
    }

    public boolean deletePhoto(String id) {
        var userId = authService.getCurrentUsername();
        return photoService.deletePhoto(userId, id);
    }
}
