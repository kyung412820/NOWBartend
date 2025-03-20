package com.nowbartend.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 유저 관련 익셉션
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 권한입니다."),

    USER_UNAUTHORIZED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),

    // Kakao 관련 익셉션
    KAKAO_FAILED_TOKEN_PARSING_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 액세스 토큰 응답 파싱 실패했습니다."),
    KAKAO_FAILED_PROFILE_PARSING_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 사용자 프로필 응답을 파싱하는데 실패했습니다."),

    // 식당 관련 익셉션
    RESTAURANT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 식당 입니다."),
    INVALID_DISTANCE_REQUEST(HttpStatus.BAD_REQUEST, "올바르지 않은 거리가 요청에 포함되어 있습니다."),

    // SeatType 관련 익셉션
    SEAT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 좌석 타입 입니다."),
    SEAT_TYPE_NAME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 좌석 타입 이름 입니다."),

    // 팔로우 관련 익셉션
    CANNOT_FOLLOW_SELF(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우 할 수 없습니다."),
    ALREADY_FOLLOWING(HttpStatus.BAD_REQUEST, "이미 팔로우 중인 관계입니다."),
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "팔로우 관계를 찾을 수 없습니다."),

    // RESTAURANT_SEAT_TYPE 관련 익셉션
    RESTAURANT_SEAT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 레스토랑 좌석입니다."),
    RESTAURANT_SEAT_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "해당 좌석의 예약이 가득 찼습니다."),
    BELOW_MIN_GUEST_COUNT(HttpStatus.BAD_REQUEST, "좌석 유형에 필요한 최소 인원보다 적습니다."),
    EXCEEDS_MAX_GUEST_COUNT(HttpStatus.BAD_REQUEST, "좌석 유형에 필요한 최대 인원보다 많습니다."),

    // 분산락 관련 익셉션
    LOCK_ACQUISITION_FAILED(HttpStatus.TOO_MANY_REQUESTS, "락 획득에 실패했습니다. 잠시 후 다시 시도해주세요."),

    // 예약 관련 익셉션
    INVALID_RESERVATION_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 예약 상태입니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약 입니다."),
    RESERVED_RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않거나 예약 상태가 아닙니다."),
    RESERVATION_COUPON_LIMIT_EXCEEDED(HttpStatus.NOT_FOUND, "할인 쿠폰 지급 이벤트가 끝났습니다."),
    DUPLICATE_RESERVATION(HttpStatus.CONFLICT, "이미 같은 시간대에 예약하신 내역이 존재합니다."),

    // Security 관련 익셉션
    LOGIN_FAILED_EXCEPTION(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다."),
    NEED_LOGIN_EXCEPTION(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    AUTHORIZATION_EXCEPTION(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다."),
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않는 JWT 서명 입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT token 입니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.BAD_REQUEST, "지원되지 않는 JWT 토큰 입니다."),
    INVALID_JWT_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않는 JWT 토큰 입니다."),
    MISSING_CREDENTIALS(HttpStatus.BAD_REQUEST, "이메일과 비밀번호를 입력해주세요."),

    // redis
    VALUE_NOT_FOUND_FOR_KEY(HttpStatus.NOT_FOUND, "해당 key에 해당하는 값이 없습니다"),

    // 지역 관련 익셉션
    REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 지역 입니다."),

    // 게시글 관련 익셉션
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시글 입니다."),
    POST_NOT_EQUAL(HttpStatus.BAD_REQUEST, "해당 게시글이 아닙니다."),

    // 댓글 관련 익셉션
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글 입니다."),

    // 좋아요 관련 익셉션
    LIKE_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 좋아요를 누르셨습니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요를 누르지 않으셨습니다"),

    // 북마크 관련 익셉션
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 북마크입니다."),
    BOOKMARK_ALREADY_EXISTS_RESTAURANT(HttpStatus.CONFLICT, "이미 북마크된 식당입니다."),

    // 컬렉션 관련 익셉션
    COLLECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 컬렉션입니다."),
    ALREADY_EXISTS_BOOKMARK_IN_COLLECTION(HttpStatus.CONFLICT, "이미 해당 컬렉션 내에 저장된 북마크입니다."),
    INVALID_ACCESS_BOOKMARK_IN_COLLECTION(HttpStatus.BAD_REQUEST, "잘못된 컬렉션 내 북마크에 대한 접근입니다."),

    // 메뉴 관련 익셉션
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 메뉴입니다."),
    MENUS_EMPTY(HttpStatus.BAD_REQUEST, "메뉴가 비어있습니다."),

    // 쿠폰 관련 익셉션
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다."),
    COUPON_ALREADY_USED(HttpStatus.CONFLICT, "이미 사용한 쿠폰입니다."),
    COUPON_ALREADY_DOWNLOAD(HttpStatus.CONFLICT, "이미 다운로드한 쿠폰입니다."),
    COUPON_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "쿠폰 이벤트가 끝났습니다."),
    COUPON_REQUEST_INVALID(HttpStatus.CONFLICT, "선착순 쿠폰은 firstComeCouponMaxCount가 1 이상이어야 합니다"),
    COUPON_GENERAL_REQUEST_INVALID(HttpStatus.CONFLICT, " 일반 쿠폰은 firstComeCouponMaxCount가 null이나 0이어야 합니다."),

    // 알림 관련 익셉션
    EMAIL_SENDING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "메일 전송에 실패했습니다."),

    // Geocoding 관련 익셉션
    GEOCODING_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Geocoding API 호출 중 오류가 발생했습니다."),
    GEOCODING_CLIENT_ERROR(HttpStatus.BAD_REQUEST, "Geocoding API 클라이언트 오류가 발생했습니다."),
    GEOCODING_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Geocoding API 서버 오류가 발생했습니다."),
    GEOCODING_NO_RESULT(HttpStatus.NOT_FOUND, "Geocoding API 결과가 없습니다."),

    // 웨이팅 관련 익셉션
    INVALID_WAITING_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 웨이팅 상태입니다."),
    INVALID_WAITING_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 웨이팅 타입입니다."),
    ALREADY_REGISTERED_USER_IN_WAITING_QUEUE(HttpStatus.CONFLICT, "이미 대기열에 등록된 고객입니다."),
    WAITING_QUEUE_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "대기열에 등록되지 않은 고객입니다."),
    WAITING_NOT_FOUND(HttpStatus.NOT_FOUND, "대기열 데이터가 존재하지 않습니다."),
    RESTAURANT_WAITING_IS_CLOSED(HttpStatus.BAD_REQUEST, "식당의 대기열이 닫혀있습니다."),
    INVALID_WAITING_CANCEL_STATUS(HttpStatus.CONFLICT, "웨이팅이 취소될 수 없는 상태입니다."),
    WAITING_STATUS_CHANGE_NOT_ALLOWED(HttpStatus.CONFLICT, "웨이팅이 변경될 수 없는 상태입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
