package com.leisan.photo.service.graphql.service;

import com.leisan.photo.service.graphql.dto.StatGql;

import java.util.List;

public interface StatService {
    List<StatGql> getStats(List<String> userIds);
}
