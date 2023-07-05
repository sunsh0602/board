package com.nhn.ep.user;

import lombok.Getter;

@Getter
public enum UserRole { // 롤을 정의하므로 enum 열거 자료형으로 작성

    ADMIN("ROLE_ADMIN"), // ADMIN 관리자
    GAMER("ROLE_GAMER"),
    USER("ROLE_USER"); // USER 사용자

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}