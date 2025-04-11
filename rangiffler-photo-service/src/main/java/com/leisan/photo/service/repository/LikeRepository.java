package com.leisan.photo.service.repository;

import com.leisan.photo.service.model.Likes;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Likes, String> {
    List<Likes> findAllByPhotoId(String photoId);
}
