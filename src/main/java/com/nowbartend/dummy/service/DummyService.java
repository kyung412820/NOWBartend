package com.nowbartend.dummy.service;

import com.nowbartend.domain.common.user.entity.User;
import com.nowbartend.domain.common.user.entity.UserRole;
import com.nowbartend.domain.common.user.repository.UserRepository;
import com.nowbartend.domain.owner.restaurant.entity.Restaurant;
import com.nowbartend.domain.owner.restaurant.repository.OwnerRestaurantRepository;
import com.nowbartend.global.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.locationtech.jts.geom.Point;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class DummyService {

    private final UserRepository userRepository;
    private final OwnerRestaurantRepository restaurantRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<User> createDummyUsers() {

        Faker faker = new Faker(new Locale("ko"));
        int count = 1000;

        List<User> users = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            User user = User.builder()
                    .email("dummy" + i + "@example.com")
                    .password(passwordEncoder.encode("password"))
                    .name(faker.name().fullName())
                    .nickname("Nick" + i)
                    .phoneNumber("01012345678")
                    .role(UserRole.OWNER)
                    .build();
            users.add(user);
        }
        userRepository.saveAll(users);

        return users;
    }

    @Async
    public void createDummyRestaurants() {

        List<User> dummyUsers = createDummyUsers();
        final int TOTAL_RECORDS = 50000;
        final int BATCH_SIZE = 1000;
        List<Restaurant> restaurantsBatch = new ArrayList<>();

        String[] districts = {
                "종로구", "중구", "용산구", "성동구", "광진구", "동대문구", "중랑구", "성북구", "강북구",
                "도봉구", "노원구", "은평구", "서대문구", "마포구", "양천구", "강서구", "구로구", "금천구",
                "영등포구", "동작구", "관악구", "서초구", "강남구", "송파구", "강동구"
        };

        String[] dongs = {"신사동", "역삼동", "압구정동", "논현동", "삼성동", "청담동", "방배동", "서초동", "대치동", "일원동"};

        // 음식종류 50가지 (예시)
        String[] foodTypes = {
                "한식", "중식", "일식", "양식", "분식", "패스트푸드", "디저트", "카페", "베이커리", "샐러드",
                "뷔페", "퓨전요리", "바베큐", "치킨", "피자", "해산물", "스테이크", "커리", "쌀국수", "타이음식",
                "멕시칸", "베트남", "인도", "아메리칸", "카레", "도시락", "샌드위치", "라멘", "우동", "국수",
                "해장국", "죽", "초밥", "토스트", "파스타", "샐러드", "디저트카페", "한정식", "찌개", "전골",
                "탕", "오믈렛", "스프", "그릴", "샤브샤브", "스시", "돈까스", "분식집", "커피전문점", "주점"
        };

        // 서울 대략적인 좌표 범위 (위도, 경도)
        // 위도: 37.5 ~ 37.7, 경도: 126.8 ~ 127.1
        for (int i = 0; i < TOTAL_RECORDS; i++) {
            // 랜덤으로 구, 동, 건물 번호 선택
            String district = districts[ThreadLocalRandom.current().nextInt(districts.length)];
            String dong = dongs[ThreadLocalRandom.current().nextInt(dongs.length)];
            int buildingNumber = ThreadLocalRandom.current().nextInt(1, 101); // 1 ~ 100

            String address = "서울특별시 " + district + " " + dong + " " + buildingNumber + "번지";

            String foodType = foodTypes[ThreadLocalRandom.current().nextInt(foodTypes.length)];

            // 임의의 더미 유저를 owner로 선택
            User owner = dummyUsers.get(ThreadLocalRandom.current().nextInt(dummyUsers.size()));

            String prefix = owner.getName();
            String restaurantName = prefix + " " + foodType;

            // 좌표 생성: 서울 내 랜덤 좌표 생성
            double lat = 37.5 + ThreadLocalRandom.current().nextDouble() * 0.2;
            double lon = 126.8 + ThreadLocalRandom.current().nextDouble() * 0.3;
            Point location = GeometryUtil.createPoint(lat, lon);

            Restaurant restaurant = Restaurant.builder()
                    .name(restaurantName)
                    .openTime(LocalTime.of(9, 0))
                    .closeTime(LocalTime.of(22, 0))
                    .breakTimeStart(LocalTime.of(14, 0))
                    .breakTimeEnd(LocalTime.of(15, 0))
                    .introduce("더미 소개")
                    .deposit(10000)
                    .reservationInterval(LocalTime.of(1, 0))
                    .owner(owner)
                    .address(address)
                    .location(location)
                    .build();

            restaurantsBatch.add(restaurant);

            if (restaurantsBatch.size() >= BATCH_SIZE) {
                restaurantRepository.saveAll(restaurantsBatch);
                log.info("저장 완료 저장된 총 레코드 수: {}", i + 1);
                restaurantsBatch.clear();
            }
        }

        if (!restaurantsBatch.isEmpty()) {
            restaurantRepository.saveAll(restaurantsBatch);
            log.info("마지막 배치 완료 저장된 총 레코드 수: {}", TOTAL_RECORDS);
        }
    }
}
