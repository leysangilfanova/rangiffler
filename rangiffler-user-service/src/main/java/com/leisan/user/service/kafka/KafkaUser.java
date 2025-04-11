package com.leisan.user.service.kafka;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record KafkaUser(String username) {

}
