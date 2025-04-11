package com.leisan.photo.service.graphql.fetchers;

import com.leisan.photo.service.graphql.dto.FeedGql;
import com.leisan.photo.service.graphql.dto.PageInfoGql;
import com.leisan.photo.service.graphql.dto.PhotoConnectionGql;
import com.leisan.photo.service.graphql.dto.PhotoEdge;
import com.leisan.photo.service.graphql.dto.PhotoGql;
import com.leisan.photo.service.graphql.dto.StatGql;
import com.leisan.photo.service.graphql.service.AuthService;
import com.leisan.photo.service.graphql.service.PhotoService;
import com.leisan.photo.service.graphql.service.StatService;
import com.leisan.photo.service.graphql.service.UserService;
import graphql.kickstart.tools.GraphQLResolver;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ExecuteOn(TaskExecutors.BLOCKING)
@Singleton
@RequiredArgsConstructor
public class FeedFieldsResolver implements GraphQLResolver<FeedGql> {
    private final StatService statService;
    private final PhotoService photoService;
    private final AuthService authService;
    private final UserService userService;

    /*@ExecuteOn(TaskExecutors.BLOCKING)
    public PhotoConnectionGql getPhotos(FeedGql feedGql,
                                        Integer page,
                                        Integer size) {
        var username = authService.getCurrentUsername();
        var userIds = new ArrayList<String>();
        userIds.add(username);
        if (feedGql.withFriends()) {
            userIds.addAll(userService.getFriends(username).block());
        }
        var photos = photoService.getPhotos(userIds, page, size);
        var count = photoService.countPhotos(userIds);
        return new PhotoConnectionGql(count, photos
                .stream()
                .map(PhotoEdge::new)
                .toList());
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    public List<StatGql> getStat(FeedGql feedGql) {
        var username = authService.getCurrentUsername();
        var userIds = new ArrayList<String>();
        userIds.add(username);
        if (feedGql.withFriends()) {
            userIds.addAll(userService.getFriends(username).block());
        }
        return statService.getStats(userIds);
    }*/

    public CompletableFuture<PhotoConnectionGql> getPhotos(FeedGql feedGql,
                                                           Integer page,
                                                           Integer size) {
        // Wrap blocking auth call in Mono.fromSupplier
        return Mono.fromSupplier(authService::getCurrentUsername)
                .subscribeOn(Schedulers.boundedElastic()) // Offload to blocking thread pool
                .flatMap(username -> {
                    // Determine friends list reactively (if needed)
                    Mono<List<String>> friendsMono = feedGql.withFriends()
                                                     ? userService.getFriends(username)
                                                     : Mono.just(Collections.emptyList());

                    return friendsMono.flatMap(friends -> {
                        List<String> userIds = new ArrayList<>();
                        userIds.add(username);
                        userIds.addAll(friends);

                        // Wrap blocking photo service calls
                        Mono<List<PhotoGql>> photosMono = Mono.fromSupplier(() ->
                                photoService.getPhotos(userIds, page, size)
                        ).subscribeOn(Schedulers.boundedElastic());

                        Mono<Integer> countMono = Mono.fromSupplier(() ->
                                photoService.countPhotos(userIds)
                        ).subscribeOn(Schedulers.boundedElastic());

                        // Combine results
                        return Mono.zip(photosMono, countMono)
                                .map(tuple -> new PhotoConnectionGql(
                                        tuple.getT2(),
                                        tuple.getT1().stream()
                                                .map(PhotoEdge::new)
                                                .toList(),
                                        new PageInfoGql(page > 0, tuple.getT1().size() == size)
                                ));
                    });
                }).toFuture();
    }

    public CompletableFuture<List<StatGql>> getStat(FeedGql feedGql) {
        return Mono.fromSupplier(authService::getCurrentUsername)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(username -> {
                    Mono<List<String>> friendsMono = feedGql.withFriends()
                                                     ? userService.getFriends(username)
                                                     : Mono.just(Collections.emptyList());

                    return friendsMono.flatMap(friends -> {
                        List<String> userIds = new ArrayList<>();
                        userIds.add(username);
                        userIds.addAll(friends);

                        return Mono.fromSupplier(() ->
                                statService.getStats(userIds)
                        ).subscribeOn(Schedulers.boundedElastic());
                    });
                }).toFuture();
    }
}
