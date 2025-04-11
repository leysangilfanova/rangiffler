package com.leisan.user.service.repository;

import com.leisan.user.service.model.User;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> find(String username);

    @Query(nativeQuery = true, value = """
            SELECT * FROM users u 
            WHERE (:query IS NULL OR u.username LIKE :query
                                           OR u.first_name LIKE :query
                                           OR u.last_name LIKE :query)
            AND u.username != :currentUsername
            ORDER BY u.id
            LIMIT :size OFFSET :page * :size
            """)
    List<User> findAllByQuery(@Nullable String query,
                              String currentUsername,
                              Integer page,
                              Integer size);

    @Query(nativeQuery = true, value = """
            SELECT count(*) FROM users u 
            WHERE (:query IS NULL OR u.username LIKE :query
                                           OR u.first_name LIKE :query
                                           OR u.last_name LIKE :query)
            AND u.username != :currentUsername
            """)
    Integer countAllByQuery(@Nullable String query,
                            String currentUsername);

    @Query(nativeQuery = true, value = """
            SELECT u.* FROM friendship f 
                JOIN users u ON f.requester_id = u.id
                WHERE f.status = 'ACCEPTED' AND f.addressee_id = :username
            """)
    List<User> findAllFriendsIncome(String username);

    @Query(nativeQuery = true, value = """
            SELECT u.* FROM friendship f 
            JOIN users u ON f.addressee_id = u.id
            WHERE f.status = 'ACCEPTED' AND f.requester_id = :username
            """)
    List<User> findAllFriendsOutcome(String username);

    @Query(nativeQuery = true, value = """
            SELECT u.* FROM friendship f
                            JOIN users u ON f.requester_id = u.id
                    WHERE f.addressee_id = :userId AND f.status = 'PENDING'
                    AND (:query IS NULL OR u.username LIKE :query
                    OR u.first_name LIKE :query
                    OR u.last_name LIKE :query)
                    ORDER BY u.id DESC
                    LIMIT :size OFFSET :page * :size
            """)
    List<User> findAllIncome(String userId,
                             @Nullable String query,
                             Integer page,
                             Integer size);

    @Query(nativeQuery = true, value = """
            SELECT count(u.username) FROM friendship f
                            JOIN users u ON f.requester_id = u.id
                    WHERE f.addressee_id = :userId AND f.status = 'PENDING'
                    AND (:query IS NULL OR u.username LIKE :query
                    OR u.first_name LIKE :query
                    OR u.last_name LIKE :query)
            """)
    Integer countAllIncome(String userId,
                           @Nullable String query);

    @Query(nativeQuery = true, value = """
            SELECT u.* FROM friendship f
                            JOIN users u ON f.addressee_id = u.id
                    WHERE f.requester_id = :userId AND f.status = 'PENDING'
                    AND (:query IS NULL OR u.username LIKE :query
                    OR u.first_name LIKE :query
                    OR u.last_name LIKE :query)
                    ORDER BY u.id DESC
                    LIMIT :size OFFSET :page * :size
            """)
    List<User> findAllOutcome(String userId,
                              @Nullable String query,
                              Integer page,
                              Integer size);

    @Query(nativeQuery = true, value = """
            SELECT count(u.id) FROM friendship f
                            JOIN users u ON f.addressee_id = u.id
                    WHERE f.requester_id = :userId AND f.status = 'PENDING'
                    AND (:query IS NULL OR u.username LIKE :query
                    OR u.first_name LIKE :query
                    OR u.last_name LIKE :query)
            """)
    Integer countAllOutcome(String userId,
                            @Nullable String query);
}
