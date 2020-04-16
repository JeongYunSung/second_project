package com.yunseong.second_project.member.command.domain;

import lombok.Getter;

@Getter
public enum Grade {

    BRONZE("Bronze", 0), SILVER("Silver", 5), GOLD("Gold", 10), PLATINUM("Platinum", 20), DIAMOND("Diamond", 30), MANAGER("Manager", Integer.MIN_VALUE), ADMIN("Admin", Integer.MAX_VALUE);

    private String value;
    private int exp;

    Grade(String value, int exp) {
        this.value = value;
        this.exp = exp;
    }

    public Grade changeGrade(int exp) {
        if (exp == Integer.MAX_VALUE) {
            return Grade.ADMIN;
        }
        else if (exp == Integer.MIN_VALUE) {
            return Grade.MANAGER;
        }
        else if (exp >= 5 && exp < 10) {
            return Grade.SILVER;
        }
        else if (exp >= 10 && exp < 20) {
            return Grade.GOLD;
        }
        else if (exp >= 20 && exp < 30) {
            return Grade.PLATINUM;
        }
        else if (exp >= 30) {
            return Grade.DIAMOND;
        }
        return Grade.BRONZE;
    }
}
