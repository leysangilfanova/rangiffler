package com.leisan.user.service.kafka;

import com.leisan.user.service.graphql.service.UserService;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@Slf4j
@KafkaListener(
        groupId = "user-service",
        pollTimeout = "500ms",
        offsetReset = OffsetReset.EARLIEST
)
@Singleton
@RequiredArgsConstructor
public class UserListener {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Topic("users")
    public void receive(ConsumerRecord<String, String> record) {
        try {
            String key = record.key();
            String value = record.value();
            log.info("Received message: Key={}, Value={}", key, value);
            var kafkaUser = objectMapper.readValue(value, KafkaUser.class);
            userService.createUser(kafkaUser);
        } catch (Exception e) {
            log.error("UserListener error: {}", e.getMessage(), e);
        }

    }
}
