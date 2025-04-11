package com.leisan.photo.service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Photo {
    @Id
    private String id;

    private String userId;

    private String src;

    private String description;

    private String countryCode;

    private Instant creationDate;
}
