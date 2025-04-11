package com.leisan.photo.service.graphql.service;

import com.leisan.photo.service.graphql.dto.PhotoGql;
import com.leisan.photo.service.graphql.dto.PhotoInputGql;
import com.leisan.photo.service.mapper.PhotoMapper;
import com.leisan.photo.service.model.Photo;
import com.leisan.photo.service.repository.LikeRepository;
import com.leisan.photo.service.repository.PhotoRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final PhotoRepository photoRepository;
    private final CountryService countryService;
    private final LikeRepository likeRepository;

    @Override
    public PhotoGql getPhoto(String id) {
        return PhotoMapper.INSTANCE.toGql(photoRepository.findById(id).orElseThrow());
    }

    @Override
    public List<PhotoGql> getPhotos(List<String> userIds,
                                    int page,
                                    int size) {
        return PhotoMapper.INSTANCE.toGqlAsList(photoRepository.findAllByUserIdIn(userIds, page, size));
    }

    public Integer countPhotos(List<String> userIds) {
        return photoRepository.countByUserIdIn(userIds);
    }

    @Transactional
    @Override
    public boolean deletePhoto(String userId,
                               String id) {
        var photo = photoRepository.findById(id);
        if (photo.isPresent()) {
            if (!photo.get().getUserId().equals(userId)) {
                return false;
            }
            var likes = likeRepository.findAllByPhotoId(photo.get().getId());
            likeRepository.deleteAll(likes);
            photoRepository.delete(photo.get());
            return true;
        } else {
            return false;
        }
    }

    @SneakyThrows
    @Transactional
    @Override
    public PhotoGql createOrUpdatePhoto(String userId,
                                        PhotoInputGql photoInputGql) {
        if (!countryService.countryExists(photoInputGql.country().code()).toFuture().get()) {
            throw new IllegalArgumentException("Country not found");
        }
        if (photoInputGql.id() == null) {
            var photo = Photo.builder()
                    .id(UUID.randomUUID().toString())
                    .src(photoInputGql.src())
                    .userId(userId)
                    .creationDate(Instant.now())
                    .description(photoInputGql.description())
                    .countryCode(photoInputGql.country().code())
                    .build();
            return PhotoMapper.INSTANCE.toGql(photoRepository.save(photo));
        } else {
            var photo = photoRepository.findByIdAndUserId(photoInputGql.id(), userId)
                    .orElseThrow();
            photo.setSrc(photoInputGql.src());
            photo.setDescription(photoInputGql.description());
            photo.setCountryCode(photoInputGql.country().code()); //todo make call to country service
            photoRepository.update(photo);
            return PhotoMapper.INSTANCE.toGql(photo);
        }
    }
}
