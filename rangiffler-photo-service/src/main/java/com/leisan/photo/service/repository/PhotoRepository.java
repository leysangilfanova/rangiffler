package com.leisan.photo.service.repository;

import com.leisan.photo.service.model.Photo;
import com.leisan.photo.service.model.StatModel;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, String> {

    Optional<Photo> findByIdAndUserId(String id,
                                      String userId);

    @Query(value = """
            SELECT * FROM photo 
                     WHERE user_id IN :userIds
            ORDER BY creation_date 
            LIMIT :pageSize OFFSET :page * :pageSize
            """, nativeQuery = true)
    List<Photo> findAllByUserIdIn(List<String> userIds,
                                  Integer page,
                                  Integer pageSize);

    Integer countByUserIdIn(List<String> userId);

    @Query(value = """
            SELECT country_code as countryCode, count(id) as photoCount FROM photo
                     WHERE user_id IN :userIds
            GROUP BY countryCode
            """, nativeQuery = true)
    List<StatModel> findStatsByUserIds(List<String> userIds);
}
