package com.nowbartend.domain.common.kakao.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Profile {

    private String nickname;
    private Boolean is_default_nickname;
}
