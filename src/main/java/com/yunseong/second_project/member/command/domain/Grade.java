package com.yunseong.second_project.member.command.domain;

import lombok.Getter;

@Getter
public enum Grade {

    BRONZE("브론즈", 0), SILVER("실버", 5), GOLD("골드", 10), PLATINUM("플래티넘", 20), DIAMOND("다이아몬드", 30), MANAGER("운영자", Integer.MIN_VALUE), ADMIN("총관리자", Integer.MAX_VALUE);

    private String value;
    private int exp;

    Grade(String value, int exp) {
        this.value = value;
        this.exp = exp;
    }
}
