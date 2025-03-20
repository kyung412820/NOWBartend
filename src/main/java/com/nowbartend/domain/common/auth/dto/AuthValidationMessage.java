package com.nowbartend.domain.common.auth.dto;

public class AuthValidationMessage {

    public static final String EMAIL_BLANK_MESSAGE = "이메일 입력은 필수입니다.";
    public static final String EMAIL_REG = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]$";
    public static final String INVALID_EMAIL_MESSAGE = "올바른 이메일 형식을 입력해주세요.";
    public static final String PASSWORD_BLANK_MESSAGE = "비밀번호 입력은 필수입니다.";
    public static final String PASSWORD_REG = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$";
    public static final String INVALID_PASSWORD_MESSAGE = "비밀번호는 대소문자, 숫자, 특수문자를 최소 1개씩 포함해야 합니다.";
    public static final int PASSWORD_MIN = 8;
    public static final int PASSWORD_MAX = 20;
    public static final String PASSWORD_RANGE_MESSAGE = "비밀번호는 8자 이상 20자 이내여야 합니다.";
    public static final String NAME_RANGE_MESSAGE = "이름은 두 글자 이상 10자 이하 실제 이름으로 입력해 주세요.";
    public static final String NAME_BLANK_MESSAGE = "이름 입력은 필수입니다.";
    public static final int NAME_MIN = 2;
    public static final int NAME_MAX = 10;
    public static final String NICKNAME_RANGE_MESSAGE = "닉네임은 한 글자 이상 10자 이하로 입력해 주세요.";
    public static final String NICKNAME_BLANK_MESSAGE = "닉네임 입력은 필수입니다.";
    public static final int NICKNAME_MIN = 1;
    public static final int NICKNAME_MAX = 10;
    public static final String PHONE_NUMBER_BLANK_MESSAGE = "전화번호 입력은 필수입니다.";
    public static final String PHONE_NUMBER_REG = "^[0-9]{11}$";
    public static final String INVALID_PHONE_NUMBER_MESSAGE = "전화번호는 11자리 숫자로 입력해야 합니다.";
    public static final String ROLE_NULL_MESSAGE = "역할을 선택해주세요";
    public static final int INTRODUCE_MAX = 35;
    public static final String INTRODUCE_RANGE_MESSAGE = "소개글은 35이하로 적어주세요.";
}
