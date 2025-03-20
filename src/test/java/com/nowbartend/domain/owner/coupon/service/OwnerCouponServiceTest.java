package com.nowbartend.domain.owner.coupon.service;

import com.nowbartend.domain.common.auth.service.AuthService;
import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.owner.coupon.dto.request.CouponCreateRequest;
import com.nowbartend.domain.owner.coupon.dto.request.CouponResponse;
import com.nowbartend.domain.owner.coupon.entity.Coupon;
import com.nowbartend.domain.owner.coupon.entity.CouponType;
import com.nowbartend.domain.owner.coupon.repository.CouponRepository;
import com.nowbartend.domain.owner.restaurant.entity.Restaurant;
import com.nowbartend.domain.owner.restaurant.service.OwnerRestaurantService;
import com.nowbartend.global.exception.CustomRuntimeException;
import com.nowbartend.global.exception.ForbiddenException;
import com.nowbartend.global.exception.error.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerCouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private OwnerRestaurantService ownerRestaurantService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private OwnerCouponService ownerCouponService;

    @Test
    @DisplayName("오너쿠폰생성_일반쿠폰(GENERAL)_성공케이스")
    public void createGeneralCoupon_success() {
        //given
        Long userId = 2L;
        Long restaurantId = 2L;
        Long ownerId = 2L;

        CouponCreateRequest request = new CouponCreateRequest("일반 쿠폰 네임", 10, 0, CouponType.GENERAL);


        //restaurant mock 생성
        Restaurant mockRestaurant = mock(Restaurant.class);
        //user의 mock을 생성
        User mockUser = mock(User.class);

        // 인증된 userId와 ownerId가 일치하는지 검사
        when(mockRestaurant.getOwner()).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(ownerId);
        //restaurantId 검사
        when(ownerRestaurantService.findById(restaurantId)).thenReturn(mockRestaurant);
        //UserAuthority 검사(패스)
        doNothing().when(authService).checkUserAuthority(ownerId, userId);


        //coupon에 값을 저장하기위해 빌더패턴 그대로 들고옴(restaurantId는 mockRestaurant로 mock 생성)
        Coupon createCoupon = Coupon.builder()
                .couponName(request.couponName())
                .discount(request.discount())
                .firstComeCouponMaxCount(request.firstComeCouponMaxCount() != null ? request.firstComeCouponMaxCount() : 0)
                .restaurant(mockRestaurant)
                .type(request.type())
                .build();
        ReflectionTestUtils.setField(createCoupon, "id", 2L);

        when(couponRepository.save(any(Coupon.class))).thenReturn(createCoupon);

        CouponResponse result = ownerCouponService.createCoupon(userId, restaurantId, request);


        assertNotNull(result);
        assertEquals(result.discount(), request.discount());
        assertEquals(result.couponName(), request.couponName());
    }

    @Test
    @DisplayName("오너쿠폰생성_선착순쿠폰(FIRST_COME)_성공케이스")
    public void createFirstComeCoupon_success() {
        //given
        Long userId = 2L;
        Long restaurantId = 2L;
        Long ownerId = 2L;

        CouponCreateRequest request = new CouponCreateRequest("선착순 쿠폰 네임", 10, 20, CouponType.FIRST_COME);

        //restaurant mock 생성
        Restaurant mockRestaurant = mock(Restaurant.class);
        //user의 mock을 생성
        User mockUser = mock(User.class);

        // 인증된 userId와 ownerId가 일치하는지 검사
        when(mockRestaurant.getOwner()).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(ownerId);
        //restaurantId 검사
        when(ownerRestaurantService.findById(restaurantId)).thenReturn(mockRestaurant);
        //UserAuthority 검사(패스)
        doNothing().when(authService).checkUserAuthority(ownerId, userId);


        //coupon에 값을 저장하기위해 빌더패턴 그대로 들고옴(restaurantId는 mockRestaurant로 mock 생성)
        Coupon createCoupon = Coupon.builder()
                .couponName(request.couponName())
                .discount(request.discount())
                .firstComeCouponMaxCount(request.firstComeCouponMaxCount() != null ? request.firstComeCouponMaxCount() : 0)
                .restaurant(mockRestaurant)
                .type(request.type())
                .build();
        ReflectionTestUtils.setField(createCoupon, "id", 2L);

        when(couponRepository.save(any(Coupon.class))).thenReturn(createCoupon);

        CouponResponse result = ownerCouponService.createCoupon(userId, restaurantId, request);


        assertNotNull(result);
        assertEquals(result.discount(), request.discount());
        assertEquals(result.couponName(), request.couponName());
    }

    @Test
    @DisplayName("오너쿠폰생성_일반쿠폰(GENERAL)_유저권한검사_실패케이스")
    public void createGeneralCoupon_authorizationFail() {
        Long userId = 2L;
        Long restaurantId = 2L;
        Long ownerId = 3L;

        CouponCreateRequest request = new CouponCreateRequest("일반 쿠폰 네임", 10, 0, CouponType.GENERAL);

        Restaurant mockRestaurant = mock(Restaurant.class);
        User mockUser = mock(User.class);

        when(mockRestaurant.getOwner()).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(ownerId);

        when(ownerRestaurantService.findById(restaurantId)).thenReturn(mockRestaurant);

        doThrow(new ForbiddenException(ErrorCode.USER_UNAUTHORIZED))
                .when(authService).checkUserAuthority(ownerId, userId);

        Assertions.assertThrows(ForbiddenException.class, () -> {
            ownerCouponService.createCoupon(userId, restaurantId, request);
        });

    }


    @Test
    @DisplayName("오너쿠폰생성_일반쿠폰(GENERAL)_잘못된수량_실패케이스")
    public void createCoupon_fail2() {
        Long userId = 2L;
        Long restaurantId = 2L;
        Long ownerId = 2L;

        CouponCreateRequest request = new CouponCreateRequest("일반 쿠폰 네임", 10, 3, CouponType.GENERAL);

        Restaurant mockRestaurant = mock(Restaurant.class);
        User mockUser = mock(User.class);

        when(mockRestaurant.getOwner()).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(ownerId);

        when(ownerRestaurantService.findById(restaurantId)).thenReturn(mockRestaurant);

        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ownerCouponService.createCoupon(userId, restaurantId, request);
        });

    }

    @Test
    @DisplayName("오너쿠폰생성_선착순쿠폰(FIRST_COME)-잘못된수량_실패케이스")
    public void createCoupon_fail3() {
        Long userId = 2L;
        Long restaurantId = 2L;
        Long ownerId = 2L;

        CouponCreateRequest request = new CouponCreateRequest(" 선착순 쿠폰 네임", 10, 0, CouponType.FIRST_COME);
        CouponCreateRequest request2 = new CouponCreateRequest(" 선착순 쿠폰 네임", 10, null, CouponType.FIRST_COME);


        Restaurant mockRestaurant = mock(Restaurant.class);
        User mockUser = mock(User.class);

        when(mockRestaurant.getOwner()).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(ownerId);

        when(ownerRestaurantService.findById(restaurantId)).thenReturn(mockRestaurant);

        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ownerCouponService.createCoupon(userId, restaurantId, request);
        });

        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ownerCouponService.createCoupon(userId, restaurantId, request2);
        });

    }
}
