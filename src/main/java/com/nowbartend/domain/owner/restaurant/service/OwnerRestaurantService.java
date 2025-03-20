package com.nowbartend.domain.owner.restaurant.service;

import com.nowbartend.domain.common.auth.service.AuthService;
import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.common.user.service.UserService;
import com.nowbartend.domain.owner.restaurant.dto.request.RestaurantCreateRequest;
import com.nowbartend.domain.owner.restaurant.dto.request.RestaurantProfileUpdateRequest;
import com.nowbartend.domain.owner.restaurant.dto.response.RestaurantFindResponse;
import com.nowbartend.domain.owner.restaurant.entity.Restaurant;
import com.nowbartend.domain.owner.restaurant.repository.OwnerRestaurantRepository;
import com.nowbartend.domain.owner.restaurantseat.service.RestaurantSeatService;
import com.nowbartend.global.exception.CustomRuntimeException;
import com.nowbartend.global.exception.NotFoundException;
import com.nowbartend.global.exception.error.ErrorCode;
import com.nowbartend.global.util.GeometryUtil;
import com.nowbartend.global.util.geocode.GeocodingClient;
import com.nowbartend.global.util.geocode.GeocodingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OwnerRestaurantService {

    private final UserService userService;
    private final AuthService authService;
    private final RestaurantSeatService restaurantSeatService;
    private final OwnerRestaurantRepository restaurantRepository;
    private final GeocodingClient geocodingClient;

    @Transactional
    public Long createRestaurant(Long userId, RestaurantCreateRequest restaurantCreateRequest) {
        User owner = userService.findUserById(userId);

        Mono<GeocodingResponse.Document> coordinatesResult = findCoordinates(restaurantCreateRequest.address());
        GeocodingResponse.Document coordinates = coordinatesResult.block();

        Restaurant restaurant = restaurantCreateRequest.toEntity(
                owner, GeometryUtil.createPoint(coordinates.latitude(), coordinates.longitude())
        );

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        restaurantSeatService.createAllRestaurantSeat(
                savedRestaurant,
                restaurantCreateRequest.restaurantSeatCreateRequestList()
        );

        return savedRestaurant.getId();
    }

    public RestaurantFindResponse findRestaurant(Long restaurantId) {
        Restaurant restaurant = findById(restaurantId);

        return RestaurantFindResponse.from(restaurant);
    }

    @Transactional
    public void updateRestaurantProfile(
            Long userId,
            Long restaurantId,
            RestaurantProfileUpdateRequest restaurantProfileUpdateRequest
    ) {
        Restaurant restaurant = findById(restaurantId);

        authService.checkUserAuthority(restaurant.getOwner().getId(), userId);

        restaurant.updateProfile(
                restaurantProfileUpdateRequest.name(),
                restaurantProfileUpdateRequest.introduce(),
                restaurantProfileUpdateRequest.deposit()
        );
    }

    @Transactional
    public void deleteRestaurant(Long userId, Long restaurantId) {
        Restaurant restaurant = findById(restaurantId);

        authService.checkUserAuthority(restaurant.getOwner().getId(), userId);

        restaurantSeatService.deleteAllRestaurantSeat(restaurant);
        restaurantRepository.delete(restaurant);
    }

    public Restaurant findById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESTAURANT_NOT_FOUND));
    }

    public Mono<GeocodingResponse.Document> findCoordinates(String address) {
        return geocodingClient.findGeocoding(address)
                .flatMap(geocodingResponse -> {
                    if (geocodingResponse != null && geocodingResponse.hasResult()) {
                        return Mono.just(geocodingResponse.findFirstResult()
                                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.GEOCODING_NO_RESULT)));
                    } else {
                        return Mono.error(new CustomRuntimeException(ErrorCode.GEOCODING_NO_RESULT));
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error while finding coordinates: {}", e.getMessage());
                    return Mono.error(new CustomRuntimeException(ErrorCode.GEOCODING_API_ERROR));
                });
    }
}
