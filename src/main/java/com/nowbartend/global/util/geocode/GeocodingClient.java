package com.nowbartend.global.util.geocode;

import com.nowbartend.global.exception.CustomRuntimeException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GeocodingClient {

    private final WebClient webClient;
    private final String kakaoApiKey;

    public GeocodingClient(@Value("${kakao.api.key}") String kakaoApiKey) {
        this.kakaoApiKey = kakaoApiKey;
        this.webClient = WebClient.create("https://dapi.kakao.com");
    }

    public Mono<GeocodingResponse> findGeocoding(String address) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/address.json")
                        .queryParam("query", address)
                        .build())
                .header("Authorization", "KakaoAK " + kakaoApiKey)
                .retrieve()
                .onStatus(status -> status.value() >= 400 && status.value() < 500, response -> {
                    log.error("Geocoding API client error: {}", response.statusCode());

                    return Mono.error(new CustomRuntimeException(ErrorCode.GEOCODING_CLIENT_ERROR));
                })
                .onStatus(status -> status.value() >= 500, response -> {
                    log.error("Geocoding API server error: {}", response.statusCode());

                    return Mono.error(new CustomRuntimeException(ErrorCode.GEOCODING_SERVER_ERROR));
                })
                .bodyToMono(GeocodingResponse.class)
                .retry(3)
                .onErrorResume(e -> {
                    log.error("Geocoding API error: {}", e.getMessage());

                    return Mono.error(new CustomRuntimeException(ErrorCode.GEOCODING_API_ERROR));
                });
    }
}
