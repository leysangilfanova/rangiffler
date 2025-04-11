package com.leisan.rangiffler.gql.api;

import com.leisan.rangiffler.gql.steps.FriendsSteps;
import com.leisan.rangiffler.gql.steps.MyTravelsSteps;
import com.leisan.rangiffler.config.Config;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class BaseApi {
    protected static final Config CFG = Config.getInstance();
    public static final FriendsApi FRIENDS_API = new FriendsApi();
    public static final UserApi USER_API = new UserApi();
    public static final TravelsApi TRAVELS_API = new TravelsApi();
    public static final FriendsSteps friendSteps = new FriendsSteps();
    public static final MyTravelsSteps myTravelsSteps = new MyTravelsSteps();

    public static final RestAssuredConfig CONFIG = RestAssured.config()
            .httpClient(HttpClientConfig.httpClientConfig()
                    .setParam("http.connection.timeout", 10000)
                    .setParam("http.socket.timeout", 10000)
                    .setParam("http.connection-manager.timeout", 10000));

    public static final RequestSpecification SPEC_GRAPHQL = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setConfig(CONFIG)
            .setBaseUri(CFG.frontUrl() + "/graphql")
            .addFilter(new ResponseLoggingFilter())
            .addFilter(new RequestLoggingFilter())
            .addFilter(new AllureRestAssured())
            .build();
}
