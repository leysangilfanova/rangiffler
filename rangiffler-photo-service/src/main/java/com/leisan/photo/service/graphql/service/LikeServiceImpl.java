package com.leisan.photo.service.graphql.service;

import com.leisan.photo.service.graphql.dto.LikeGql;
import com.leisan.photo.service.graphql.dto.PhotoInputGql;
import com.leisan.photo.service.mapper.LikeMapper;
import com.leisan.photo.service.model.Likes;
import com.leisan.photo.service.repository.LikeRepository;
import com.leisan.photo.service.repository.PhotoRepository;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PhotoRepository photoRepository;

    @Override
    public List<LikeGql> getPhotoLikes(String photoId) {
        return LikeMapper.INSTANCE.toGqlAsList(likeRepository.findAllByPhotoId(photoId));
    }

    @Override
    public void addLike(String username,
                        PhotoInputGql photoInput) {
        if (photoInput.like() == null) {
            throw new IllegalArgumentException("Like must have a like");
        }
        var photo = photoRepository.findById(photoInput.id()).orElseThrow();
        var likeUserId = photoInput.like().user();
        var photoLikes = likeRepository.findAllByPhotoId(photo.getId());
        var hasLike = photoLikes.stream().filter(likeGql -> likeGql.getUserId().equals(likeUserId)).findFirst();
        if (hasLike.isPresent()) {
            likeRepository.delete(hasLike.get());
            return;
        }
        var like = Likes.builder()
                .id(UUID.randomUUID().toString())
                .creationDate(Instant.now())
                .userId(likeUserId)
                .photoId(photo.getId())
                .build();
        likeRepository.save(like);
    }
}
