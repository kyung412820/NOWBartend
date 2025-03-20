package com.nowbartend.global.util.geocode;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;
import java.util.Optional;

@Builder(access = AccessLevel.PRIVATE)
public record GeocodingResponse(
        @JsonProperty("documents") List<Document> documents
) {

    public boolean hasResult() {
        return documents != null && !documents.isEmpty();
    }

    public Optional<Document> findFirstResult() {
        return hasResult() ? Optional.of(documents.get(0)) : Optional.empty();
    }

    public static GeocodingResponse from(List<Document> documents) {
        return GeocodingResponse.builder()
                .documents(documents)
                .build();
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record Document(
            String addressName,
            @JsonProperty("x") Double longitude,
            @JsonProperty("y") Double latitude
    ) {

        public static Document from(String addressName, Double longitude, Double latitude) {
            return Document.builder()
                    .addressName(addressName)
                    .longitude(longitude)
                    .latitude(latitude)
                    .build();
        }
    }
}