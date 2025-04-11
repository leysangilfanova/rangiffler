package com.leisan.country.service;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.qameta.allure.SeverityLevel.CRITICAL;

@Tag("unit")
@Feature("Карта")
class QueryTest extends BaseTest {

    @Test
    @DisplayName("Проверка списка стран")
    @Severity(CRITICAL)
    void testGetCountriesSuccess() throws IOException, JSONException {
        var expectedRs = Files.readString(Path.of("src/test/resources/expected_countries_rs.json"));
        var query = """
                query MyQuery {
                    countries {
                        code
                        flag
                        name
                    }
                  }
                """;
        var countriesRq = GraphqlRq.builder()
                .query(query)
                .operationName("MyQuery")
                .build();

        var countriesRs = graphqlClient.makeGraphqlCall(countriesRq);
        JSONAssert.assertEquals(expectedRs, countriesRs, false);
    }
}
