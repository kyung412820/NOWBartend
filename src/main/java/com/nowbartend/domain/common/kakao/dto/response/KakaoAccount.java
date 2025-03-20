package com.nowbartend.domain.common.kakao.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoAccount {

    private Profile profile;
    private Boolean profileNicknameNeedsAgreement;

    private String name;
    private Boolean name_needs_agreement;

    private String email;
    private Boolean isEmailValid;
    private Boolean isEmailVerified;
    private Boolean hasEmail;
    private Boolean emailNeedsAgreement;

    private String phoneNumber;
    private Boolean hasPhoneNumber;
    private Boolean phoneNumberNeedsAgreement;

}
