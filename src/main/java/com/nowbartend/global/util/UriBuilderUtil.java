package com.nowbartend.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UriBuilderUtil {

    public static URI create(String path, Long id) {
        return UriComponentsBuilder.fromPath(path)
                .buildAndExpand(id)
                .toUri();
    }
}
