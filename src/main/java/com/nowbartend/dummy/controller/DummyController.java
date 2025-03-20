package com.nowbartend.dummy.controller;

import com.nowbartend.dummy.service.DummyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/api")
@RequiredArgsConstructor
public class DummyController {

    private final DummyService dummyService;

    @PostMapping("/dummy/restaurants")
    public ResponseEntity<String> dummyUsersAndRestaurants() {
        dummyService.createDummyRestaurants();
        return ResponseEntity.ok("더미 생성 작업 시작");
    }
}
