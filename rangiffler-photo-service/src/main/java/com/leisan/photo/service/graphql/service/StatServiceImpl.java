package com.leisan.photo.service.graphql.service;

import com.leisan.photo.service.graphql.dto.StatGql;
import com.leisan.photo.service.repository.PhotoRepository;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.ExecutionException;


@Singleton
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final PhotoRepository photoRepository;
    private final CountryService countryService;

    @Override
    public List<StatGql> getStats(List<String> userIds) {
        var stats = photoRepository.findStatsByUserIds(userIds);
        return stats.stream()
                .map(v -> {
                    try {
                        return new StatGql(v.getPhotoCount(),
                                countryService.getCountry(v.getCountryCode()).toFuture().get());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }
}
