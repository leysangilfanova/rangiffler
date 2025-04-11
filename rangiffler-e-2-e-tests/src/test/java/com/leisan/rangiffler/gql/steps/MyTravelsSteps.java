package com.leisan.rangiffler.gql.steps;

import com.leisan.rangiffler.gql.dto.CountryResponse;
import com.leisan.rangiffler.gql.dto.PhotoResponse;
import com.leisan.rangiffler.gql.dto.FeedResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static com.leisan.rangiffler.gql.api.BaseApi.TRAVELS_API;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MyTravelsSteps {

    public record ExpectedPhoto(
            String id,
            String srcPrefix,
            String countryCode,
            String countryName,
            String description,
            int likesCount,
            List<String> likedByUsers
    ) {}

    public record ExpectedStat(int count, String countryCode) {}

    @Step("Проверка ответа ленты")
    public void assertFeedResponse(FeedResponse feedResponse,
                                    List<ExpectedPhoto> expectedPhotos,
                                    List<ExpectedStat> expectedStats,
                                    boolean expectedHasPreviousPage,
                                    boolean expectedHasNextPage) {
        // Проверка основного ответа
        assertThat(feedResponse.getData()).isNotNull();
        assertThat(feedResponse.getData().getFeed()).isNotNull();

        // Проверка фотографий
        List<FeedResponse.Edge> edges = feedResponse.getData().getFeed().getPhotos().getEdges();
        assertThat(edges).hasSize(expectedPhotos.size());

        for (int i = 0; i < expectedPhotos.size(); i++) {
            ExpectedPhoto expected = expectedPhotos.get(i);
            FeedResponse.PhotoNode actual = edges.get(i).getNode();

            assertPhotoNode(actual, expected);
        }

        // Проверка пагинации
        FeedResponse.PageInfo pageInfo = feedResponse.getData().getFeed().getPhotos().getPageInfo();
        assertThat(pageInfo.isHasPreviousPage()).isEqualTo(expectedHasPreviousPage);
        assertThat(pageInfo.isHasNextPage()).isEqualTo(expectedHasNextPage);

        // Проверка статистики
        List<FeedResponse.Stat> stats = feedResponse.getData().getFeed().getStat();
        for (int i = 0; i < expectedStats.size(); i++) {
            ExpectedStat expected = expectedStats.get(i);
            FeedResponse.Stat actual = stats.get(i);

            assertThat(actual.getCount())
                    .as("Проверка count для элемента #" + i)
                    .isEqualTo(expected.count());

            assertThat(actual.getCountry().getCode())
                    .as("Проверка countryCode для элемента #" + i)
                    .isEqualTo(expected.countryCode());
        }
    }

    @Step("Проверка статистика фото по стране")
    public void countriesStatAssert(FeedResponse feedResponse, List<ExpectedStat> expectedStats) {
        List<FeedResponse.Stat> stats = feedResponse.getData().getFeed().getStat();
        assertThat(stats).hasSize(expectedStats.size());

        for (int i = 0; i < expectedStats.size(); i++) {
            ExpectedStat expected = expectedStats.get(i);
            FeedResponse.Stat actual = stats.get(i);
            assertStat(actual, expected);
        }
    }

    @Step("Проверка фотографии")
    public void assertPhotoNode(FeedResponse.PhotoNode actual, ExpectedPhoto expected) {
        assertThat(actual.getId()).isEqualTo(expected.id());
        assertThat(actual.getSrc()).matches("data:image/(png|jpe?g);base64,.*");

        assertThat(actual.getCountry()).isNotNull();
        assertThat(actual.getCountry().getCode()).isEqualTo(expected.countryCode());
        assertThat(actual.getCountry().getName()).isEqualTo(expected.countryName());

        assertThat(actual.getDescription()).isEqualTo(expected.description());

        assertThat(actual.getLikes()).isNotNull();
        assertThat(actual.getLikes().getTotal()).isEqualTo(expected.likesCount());

        if (expected.likedByUsers().isEmpty()) {
            assertThat(actual.getLikes().getLikes()).isEmpty();
        } else {
            assertThat(actual.getLikes().getLikes())
                    .extracting(FeedResponse.Like::getUser)
                    .containsExactlyInAnyOrderElementsOf(expected.likedByUsers());
        }
    }

    @Step("Проверка статистики")
    private void assertStat(FeedResponse.Stat actual, ExpectedStat expected) {
        assertThat(actual.getCount()).isEqualTo(expected.count());
        assertThat(actual.getCountry()).isNotNull();
        assertThat(actual.getCountry().getCode()).isEqualTo(expected.countryCode());
    }

    @Step("Проверка создания фотографии")
    public void createPhotoAssert(PhotoResponse photoResponse,
                                  String expectedSrcPrefix,
                                  String expectedDescription,
                                  String expectedCountryCode,
                                  int expectedTotalLikes,
                                  Response response) {
        // Проверка, что ID фото не пустой
        assertThat(photoResponse.getData().getPhoto().getId()).as("Photo ID should not be blank").isNotBlank();

        // Проверка фото
        assertThat(photoResponse.getData().getPhoto().getSrc())
                .as("Photo src should start with expected prefix")
                .isEqualTo(expectedSrcPrefix);

        // Проверка описания фото
        assertThat(photoResponse.getData().getPhoto().getDescription())
                .as("Photo description should match expected value")
                .isEqualTo(expectedDescription);

        // Проверка кода страны
        assertThat(photoResponse.getData().getPhoto().getCountry().getCode())
                .as("Country code should match expected value")
                .isEqualTo(expectedCountryCode);

        // Проверка количества лайков
        assertThat(photoResponse.getData().getPhoto().getLikes().getTotal())
                .as("Total likes count should match expected value")
                .isEqualTo(expectedTotalLikes);

        // Проверка отсутствия ошибок в ответе
        assertNull(response.path("errors"),
                "Ответ содержит ошибку: " + response.asString());
    }

    @Step("Создание нового поста")
    public String createPhotoAndGetId(String token, String photo, String description, String countryCode) {
        Response response = TRAVELS_API.createPhoto(token, photo, description, countryCode);
        PhotoResponse photoResponse = response.as(PhotoResponse.class);
        return photoResponse.getData().getPhoto().getId();
    }

    public void assertCountriesAreValid(CountryResponse countryResponse) {
        List<CountryResponse.Country> countries = countryResponse.getData().getCountries();

        for (int i = 0; i < countries.size(); i++) {
            CountryResponse.Country country = countries.get(i);

            assertThat(country.getCode())
                    .as("Проверка code у страны #%d", i)
                    .isNotBlank();

            assertThat(country.getName())
                    .as("Проверка name у страны #%d", i)
                    .isNotBlank();

            assertThat(country.getFlag())
                    .as("Проверка flag у страны #%d", i)
                    .isNotBlank()
                    .matches("^data:image/(png|jpe?g);base64,.*");
        }
    }
}
